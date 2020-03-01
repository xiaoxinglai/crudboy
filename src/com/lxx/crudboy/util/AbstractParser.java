package com.lxx.crudboy.util;

import com.lxx.crudboy.model.ReadResult;

import java.util.HashMap;
import java.util.Stack;

/**
 * @ClassName AbstractParser
 * @Author laixiaoxing
 * @Date 2020/2/29 下午8:33
 * @Description 表达式解析
 * @Version 1.0
 */
public interface  AbstractParser {

    /**
     * 解析Test里面的内容 并返回结果
     * 使用两个栈，分别是操作数栈 存储数字 和操作符栈 存储运算符
     * 读入表达式时
     * 如果是操作数 则入操作数栈
     * 如果是运算符 则入操作符栈
     * 当运算符入栈时，和操作符栈栈顶元素比较优先级
     * 如果优先级比栈顶元素高，则入栈,并接收下一个字符
     * 如果和栈顶元素相同，则脱括号 并接收下一个字符 （ 因为相同只有( )括号）
     * 如果小于栈顶元素优先级，则操作数出栈，操作符出栈 并计算运算结果再入栈
     *
     * @param test
     * @param param
     * @return
     */
    default boolean parserText(String test, HashMap<String, Object> param) throws Exception {
        test = test.trim().replaceAll("(?<=\\s)\\s", "");
        char[] target = test.toCharArray();
        //操作数栈
        Stack<String> stackData = new Stack<>();
        //操作符栈
        Stack<String> stackOp = new Stack<>();
        ReadResult readResult = new ReadResult();
        readResult.setI(0);
        while (readResult.getI() < target.length) {
            readResult = read(target, readResult);
            String data = readResult.getRead();

            //判断是否是操作数
            if (isOPN(data)) {
                stackData.push(data);
            } else {
                switch (stackOp.empty() ? 1 : compareOp(data, stackOp.peek())) {
                    case 1:
                        //优先级比栈顶元素大 ，则入栈,并接收下一个字符
                        stackOp.push(data);
                        break;
                    case 0:
                        //优先级和栈顶元素相同 脱去括号
                        stackOp.pop();
                        break;
                    case -1:
                        //优先级比栈顶元素小，则操作数出栈，操作符出栈，运算之后入栈。即意思是优先级高的先运算
                        //顺序靠后的操作数
                        String b = stackData.pop();
                        //顺序靠前的操作数
                        String a = stackData.pop();
                        stackData.push(Option(a, b, stackOp.pop(), param));
                        readResult.setI(readResult.getI() - data.length());
                        break;
                    default:
                        throw new Exception("运算符优先级异常");
                }
            }
        }
        while (!stackOp.empty()) {
            String b = stackData.pop();
            //顺序靠前的操作数
            String a = stackData.pop();
            stackData.push(Option(a, b, stackOp.pop(), param));
        }
        return Boolean.valueOf(stackData.pop());

    }

    /**
     * 运算操作
     * @param a
     * @param b
     * @param pop
     * @param param
     * @return
     */
      String Option(String a, String b, String pop, HashMap<String, Object> param)throws Exception;

    /**
     * 比较优先级
     * @param data
     * @param peek
     * @return
     */
      int compareOp(String data, String peek);

    /**
     * 是否是操作数
     * @param data
     * @return
     */
      boolean isOPN(String data);

    /**
     * 读取字符
     * @param target
     * @param readResult
     * @return
     */
     ReadResult read(char[] target, ReadResult readResult);


}
