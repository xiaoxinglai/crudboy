package com.lxx.crudboy.util.sqlfilte;

import com.lxx.crudboy.model.ReadResult;
import com.lxx.crudboy.util.AbstractParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName AbstractSqlParserFilter
 * @Author laixiaoxing
 * @Date 2020/2/24 下午11:48
 * @Description 抽象sql解析
 * @Version 1.0
 */
public abstract class AbstractSqlParserFilter  implements SqlParserFilter,AbstractParser {


    /**
     * 返回首字符大写
     *
     * @param item
     * @return
     */
    protected String ToUp(String item) {
        char first = item.toCharArray()[0];
        String old = String.valueOf(first);
        String newFirst = String.valueOf(old.toUpperCase());
       return item.replaceFirst(old,newFirst);
    }




    /**
     * 获取属性
     *
     * @param property
     * @param trim
     * @return
     */
    protected String getProperty(String property, String trim) {
        Matcher matcherPrefix = getMatcher("(?<=" + property + "=\").*?(?=\")", trim);
        if (matcherPrefix.find()) {
            return matcherPrefix.group(0).trim();
        }
        return StringUtils.EMPTY;
    }


    protected Matcher getMatcher(String patter, String sql) {
        Pattern pattern = Pattern.compile(patter, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(sql);
    }




    /**
     * 运算操作
     *
     * @param a
     * @param b
     * @param pop
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public String Option(String a, String b, String pop, HashMap<String, Object> param) throws Exception {
        switch (pop) {
            case "and":
                return String.valueOf((Boolean.valueOf(a) && Boolean.valueOf(b)));
            case "or":
                return String.valueOf((Boolean.valueOf(a) || Boolean.valueOf(b)));
            case "!=":
                return String.valueOf(!(param.get(a)==null?"null":param.get(a)).equals(param.get(b) == null ? b : param.get(b)));
            case "=":
                return String.valueOf((param.get(a)==null?"null":param).equals(param.get(b) == null ? b : param.get(b)));
            default:
                throw new Exception("异常输入");
        }
    }


    /**
     * 读取操作数和操作符
     *
     * @param target
     * @param readResult
     * @return
     */
    @Override
    public ReadResult read(char[] target, ReadResult readResult) {
        int i = readResult.getI();
        while (space(target[i])) {
            i++;
        }
        int start = i;
        if (left(target[i])) {
            readResult.setRead("(");
            i = i + 1;
            readResult.setI(i);
            return readResult;
        }
        if (right(target[i])) {
            readResult.setRead(")");
            i = i + 1;
            readResult.setI(i);
            return readResult;
        }
        if (notEq(target, i)) {
            readResult.setRead("!=");
            i = i + 2;
            readResult.setI(i);
            return readResult;
        }
        if (eq(target, i)) {
            readResult.setRead("=");
            i = i + 1;
            readResult.setI(i);
            return readResult;
        }
        if (or(target, i)) {
            readResult.setRead("or");
            i = i + 2;
            readResult.setI(i);
            return readResult;
        }
        if (and(target, i)) {
            readResult.setRead("and");
            i = i + 3;
            readResult.setI(i);
            return readResult;
        }
        while (i < target.length && (notOp(target, i) && !space(target[i]))) {
            i++;
        }
        String res = "";
        for (int i1 = start; i1 < i; i1++) {
            res += target[i1];
        }
        readResult.setRead(res);
        readResult.setI(i);
        return readResult;
    }

    /**
     * 是否是操作符
     *
     * @param c
     * @return
     */
    @Override
    public boolean isOPN(String c) {
        if (c.equals("and") || c.equals("AND") || c.equals("OR") || c.equals("or") || c.equals("(") || c.equals(")")
                || c.equals("!=") || c.equals("=")) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断不是操作符
     *
     * @param target
     * @param i
     * @return
     */
    public boolean notOp(char[] target, int i) {
        if (and(target, i) || or(target, i) || eq(target, i) || notEq(target, i) || left(target[i]) || right(
                target[i])) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断是否是and
     *
     * @param target
     * @param i
     * @return
     */
    private boolean and(char[] target, int i) {
        if (i - 1 > 0 && i + 3 < target.length) {
            if (target[i - 1] == ' ' && (target[i] == 'a' || target[i] == 'A') && (target[i + 1] == 'n'
                    || target[i + 1] == 'N') && (target[i + 2] == 'd' || target[i + 2] == 'D')
                    && target[i + 3] == ' ') {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是or
     *
     * @param target
     * @param i
     * @return
     */
    private boolean or(char[] target, int i) {
        if (i - 1 > 0 && i + 2 < target.length) {
            if (target[i - 1] == ' ' && (target[i] == 'o' || target[i] == 'O') && (target[i + 1] == 'r'
                    || target[i + 1] == 'R') && target[i + 2] == ' ') {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否相等
     *
     * @param target
     * @param i
     * @return
     */
    private boolean eq(char[] target, int i) {
        if (1 < target.length) {
            if (target[i] == '=') {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否不相等
     *
     * @param target
     * @param i
     * @return
     */
    private boolean notEq(char[] target, int i) {
        if (i + 1 < target.length) {
            if (target[i] == '!' && target[i + 1] == '=') {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是右括号
     *
     * @param c
     * @return
     */
    private boolean right(char c) {
        return c == ')';
    }

    /**
     * 判断是否是左括号
     *
     * @param c
     * @return
     */
    private boolean left(char c) {
        if (c == '(') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是空格
     *
     * @param c
     * @return
     */
    private boolean space(char c) {
        if (c == ' ') {
            return true;
        }
        return false;
    }


    /**
     * 比较算符优先级
     *
     * @param op1 最新的操作符
     * @param op2 上一个操作符
     * @return
     */
    @Override
    public int compareOp(String op1, String op2) {
        if (op1.equals("(")) {
            if (op2.equals(")")) {
                return -1;
            }
            return 1;
        }
        if (op1.equals(")")) {
            if (op2.equals("(")) {
                return 0;
            }
            return -1;
        }

        if (op1.equals("and")) {
            if (Arrays.asList("and", "=", "!=", ")").contains(op2)) {
                return -1;
            }
            return 1;
        }

        if (op1.equals("or")) {
            if (Arrays.asList("!=", "=", "and", "or", ")").contains(op2)) {
                return -1;
            }
            return 1;
        }
        if (op1.equals("!=")) {
            if (Arrays.asList("!=", "=", ")").contains(op2)) {
                return -1;
            }
            return 1;
        }
        if (op1.equals("=")) {
            if (Arrays.asList("!=", "=", ")").contains(op2)) {
                return -1;
            }
            return 1;
        }
        return -2;
    }


}
