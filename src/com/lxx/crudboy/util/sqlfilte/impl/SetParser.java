package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;

import java.util.HashMap;
import java.util.regex.Matcher;


/**
 * @ClassName whereParser
 * @Author laixiaoxing
 * @Date 2020/2/26 下午9:15
 * @Description set标签
 * @Version 1.0
 */
public class SetParser extends AbstractSqlParserFilter {
    @Override
    public String parser(String sql, HashMap<String, Object> param) throws Exception {
        Matcher matcherSet = getMatcher("<set>", sql);
        if (matcherSet.find()) {
            sql = matcherSet.replaceAll("<trim prefix=\"SET\" suffixOverrides=\",\">");
        }
        Matcher matcherEnd = getMatcher("</set>", sql);
        if (matcherEnd.find()) {
            sql = matcherEnd.replaceAll("</trim>");
        }

        return sql;
    }


    @Override
    public int order() {
        return 35;
    }

}
