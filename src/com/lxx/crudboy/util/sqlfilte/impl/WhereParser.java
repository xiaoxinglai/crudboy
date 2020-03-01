package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;

import java.util.HashMap;
import java.util.regex.Matcher;


/**
 * @ClassName whereParser
 * @Author laixiaoxing
 * @Date 2020/2/26 下午9:15
 * @Description where标签
 * @Version 1.0
 */
public class WhereParser extends AbstractSqlParserFilter {
    @Override
    public String parser(String sql, HashMap<String, Object> param) throws Exception {
        Matcher matcherWhere=  getMatcher("<where>",sql);
        if (matcherWhere.find()) {
            sql= matcherWhere.replaceAll("<trim prefix=\"WHERE\" prefixOverrides=\"AND |OR \">");
        }

        Matcher matcherEnd=  getMatcher("</where>",sql);
        if (matcherEnd.find()) {
            sql= matcherEnd.replaceAll("</trim>");
        }

        return sql;
    }


    @Override
    public int order() {
        return 30;
    }

}
