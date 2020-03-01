package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @ClassName TrimParser
 * @Author laixiaoxing
 * @Date 2020/2/26 下午9:52
 * @Description trim标签
 * @Version 1.0
 */
public class TrimParser extends AbstractSqlParserFilter {


    @Override
    public String parser(String sql, HashMap<String, Object> param) throws Exception {
        //截取最外面的trim标签
        Matcher mTrim = getMatcher("<trim(.|\\s)*<\\/trim>", sql);
        if (mTrim.find()) {
            String sqlTrim = mTrim.group(0).trim();
            //截取trim标签包含的内容
            Matcher mTrimText = getMatcher("trim\\s.*(?=>)", sqlTrim);
            Stack<String> trimStack = new Stack<>();
            while (mTrimText.find()) {
                sqlTrim = mTrimText.group(0).trim();
                trimStack.push(sqlTrim);
            }


            while (!trimStack.empty()) {
                String trim = trimStack.pop();
                //解析 trim 属性
                //解析prefix
                String prefix = getProperty("prefix", trim);
                //解析prefixOverrides
                String[] prefixOverrides = getTrimOverridesProperty("prefixOverrides", trim);
                //解析suffix
                String suffix = getProperty("suffix", trim);
                //解析suffixOverrides
                String[] suffixOverrides = getTrimOverridesProperty("suffixOverrides", trim);


                if (prefixOverrides!=null&&prefixOverrides.length>1){
                    Matcher matcherPrefix = getMatcher("(?<=prefixOverrides=\").*?(?=\")", trim);
                    trim= matcherPrefix.replaceAll(".{0,1000}");
                }

                if (suffixOverrides!=null&&suffixOverrides.length>1){
                    Matcher matcherPrefix = getMatcher("(?<=suffixOverrides=\").*?(?=\")", trim);
                    trim= matcherPrefix.replaceAll(".{0,1000}");
                }

                //sql里面截取内容
                Matcher matcherPart = getMatcher("(?<=" + trim + ">)(\\s|.)*?(?=<\\/trim>)", sql);
                if (matcherPart.find()) {
                    String oldContent = matcherPart.group(0).trim();
                    //修改原内容得到新内容
                    String newContent = oldContent;

                    if (prefixOverrides != null && prefixOverrides.length > 0) {
                        for (int i = 0; i < prefixOverrides.length; i++) {
                            if (oldContent.startsWith(prefixOverrides[i])) {
                                newContent = oldContent.replaceFirst(prefixOverrides[i], " ");
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty(prefix)) {
                        newContent = prefix+" " + newContent;
                    }


                    if (suffixOverrides != null && suffixOverrides.length > 0) {
                        for (int i = 0; i < suffixOverrides.length; i++) {
                            if (oldContent.endsWith(suffixOverrides[i])) {
                                newContent = newContent.replaceFirst(suffixOverrides[i], " ");
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty(suffix)) {
                        newContent = newContent + " "+suffix;
                    }

                    sql = sql.replaceFirst("<.*\\s*" + oldContent + "\\s*<\\/trim>", newContent);
                }

            }
        }
        return sql;
    }


    /**
     * 解析要覆盖的标签
     *
     * @param Overrides
     * @param trim
     * @return
     */
    private String[] getTrimOverridesProperty(String Overrides, String trim) {
        String suffix = getProperty(Overrides, trim);
        if (StringUtils.isNotEmpty(suffix)) {
            return suffix.split("\\|");

        }
        return null;
    }




    @Override
    public int order() {
        return 50;
    }
}
