package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @ClassName ForeachParser
 * @Author laixiaoxing
 * @Date 2020/2/27 下午1:49
 * @Description foreache标签解析
 * @Version 1.0
 */
public class ForeachParser extends AbstractSqlParserFilter {


    @Override
    public String parser(String sql, HashMap<String, Object> param) throws Exception {
        Matcher foreach = getMatcher("<foreach(.|\\s)*<\\/foreach>", sql);
        if (foreach.find()) {
            String foreachText = foreach.group(0).trim();
            //解析属性
            String item = getProperty("item", foreachText);
            String index = getProperty("index", foreachText);
            String collection = getProperty("collection", foreachText);
            String open = getProperty("open", foreachText);
            String separator = getProperty("separator", foreachText);
            String close = getProperty("close", foreachText);
            //解析foreach包含的内容
            Matcher foreachContent = getMatcher("(?<=\\>)(\\s*.*)(?=\\s*<\\/foreach>)", sql);
            if (foreachContent.find()) {
                String content = foreachContent.group(0).trim();
                StringBuilder forContext = new StringBuilder();
                forContext.append(open);
                if (param.get(collection) instanceof List) {
                    List list = (List) param.get(collection);
                    int i=0;
                    for (Object o : list) {
                        String newContent = "";
                        if (o instanceof String) {
                            newContent = content.replace("#{" + item + "}", "'" + (String)o + "'");
                        } else {
                            Method method = o.getClass().getMethod("get" + ToUp(item));
                            Object value = method.invoke(o);
                            newContent = content.replace("#{" + item + "}", "'" + value.toString() + "'");
                        }
                        forContext.append(newContent);
                        if (i<list.size()-1){
                            forContext.append(separator);
                        }
                        i++;
                    }
                }
                forContext.append(close);
                //替换
                sql = foreach.replaceAll(forContext.toString());
            }
        }
        return sql;
    }


    @Override
    public int order() {
        return 60;
    }
}
