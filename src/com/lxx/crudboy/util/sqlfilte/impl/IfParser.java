package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;

import java.util.*;
import java.util.regex.Matcher;


/**
 * @ClassName DefulterParser
 * @Author laixiaoxing
 * @Date 2020/2/17 下午1:59
 * @Description if解析器
 * @Version 1.0
 */
public class IfParser extends AbstractSqlParserFilter {


    @Override
    public String parser(String sql, HashMap<String, Object> param) throws Exception {
        //截取所有的if标签
        Matcher mif = getMatcher( "<if(.|\\s)*<\\/if>", sql);
        if (mif.find()) {
            //if标签内的值
            List<String> value = new ArrayList<>();
            List<String> test = new ArrayList<>();
            String sqlIf = mif.group(0).trim();
            //取if标签内的值
            Matcher mifBlock = getMatcher( "(?<=\\>)(\\s*.*)(?=\\s*<\\/if>)", sqlIf);
            while (mifBlock.find()) {
                value.add(mifBlock.group(0));
            }
            //获取test内的值
            Matcher mifTest = getMatcher( "(?<=test\\=\")(\\s*.*)(?=\\s*\")", sqlIf);
            while (mifTest.find()) {
                test.add(mifTest.group(0));
            }

            //根据test内的结果来拼if标签内的值
            for (int i = 0; i < test.size(); i++) {
                //Test的结果
                if (parserText(test.get(i), param)) {
                    String res = value.get(i);
                    for (Map.Entry<String, Object> kv : param.entrySet()) {
                        res = res.replace("#{" + kv.getKey() + "}", "'" + kv.getValue() + "'");
                    }
                    sql = sql.replaceAll("<if.*\\s*"+test.get(i)+".*\\s*.*\\s*</if>", res);
                }else {
                   sql= sql.replaceAll("<if.*\\s*"+test.get(i)+".*\\s*.*\\s*</if>","");
                }
            }
        }
        return sql;
    }



    @Override
    public int order() {
        return 10;
    }


}
