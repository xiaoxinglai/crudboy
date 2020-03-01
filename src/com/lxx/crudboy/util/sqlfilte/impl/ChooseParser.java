package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @ClassName ChoosepParser
 * @Author laixiaoxing
 * @Date 2020/2/26 下午7:27
 * @Description Choose标签解析
 * @Version 1.0
 */
public class ChooseParser extends AbstractSqlParserFilter {

    @Override
    public String parser(String sql, HashMap<String, Object> param) throws Exception {
        //截取choose标签内的值
        Matcher mCS = getMatcher("(?<=<choose>)(.|\\s)*(?=<\\/choose>)", sql);
        if (mCS.find()) {
            //when标签内的值
            List<String> value = new ArrayList<>();
            List<String> test = new ArrayList<>();
            String sqlWhen = mCS.group(0).trim();
            //取when标签内的值
            Matcher mCsBlock = getMatcher("(?<=\\>)(\\s*.*)(?=\\s*<\\/when>)", sqlWhen);
            while (mCsBlock.find()) {
                value.add(mCsBlock.group(0));
            }
            //获取test内的值
            Matcher mifTest = getMatcher("(?<=test\\=\")(\\s*.*)(?=\\s*\")", sqlWhen);
            while (mifTest.find()) {
                test.add(mifTest.group(0));
            }

            Boolean tagWhen = false;
            //根据test内的结果来拼when标签内的值
            for (int i = 0; i < test.size(); i++) {
                //Test的结果
                if (parserText(test.get(i), param)) {
                    tagWhen = true;
                    String res = value.get(i);
                    for (Map.Entry<String, Object> kv : param.entrySet()) {
                        res = res.replace("#{" + kv.getKey() + "}", "'" + kv.getValue() + "'");
                    }
                    sql = sql.replaceFirst("<when.*\\s*" + test.get(i) + ".*\\s*.*\\s*</when>", res);
                } else {
                    sql = sql.replaceFirst("<when.*\\s*" + test.get(i) + ".*\\s*.*\\s*</when>", "");
                }
            }
            Matcher mOW = getMatcher("(?<=\\<otherwise\\>)(.|\\s)*(?=\\<\\/otherwise\\>)", sql);
            while (mOW.find()) {
                if (tagWhen) {
                    //有when匹配到 清除otherwise
                    sql = sql.replaceAll("<otherwise>(.|\\s)* <\\/otherwise>", "");

                } else {
                    //没有when匹配到
                    String other = mOW.group(0);
                    if (StringUtils.isNotEmpty(other)) {
                        sql = sql.replaceAll("<otherwise>(.|\\s)* <\\/otherwise>", other);
                    }
                }
            }
            sql = sql.replaceAll("<choose>", "").replaceAll("</choose>", "");
        }
        return sql;
    }

    @Override
    public int order() {
        return 20;
    }
}
