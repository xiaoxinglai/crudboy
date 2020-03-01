package com.lxx.crudboy.util.sqlfilte.impl;

import com.lxx.crudboy.util.sqlfilte.AbstractSqlParserFilter;
import com.lxx.crudboy.util.sqlfilte.SqlParserFilter;

import java.util.HashMap;
import java.util.regex.Matcher;


/**
 * @ClassName DefulterParser
 * @Author laixiaoxing
 * @Date 2020/2/17 下午1:59
 * @Description 默认解析器
 * @Version 1.0
 */
public class DefaulterParser extends AbstractSqlParserFilter {


    /**
     * (?<=>)(\s|.)*(?=<)
     * 截取select|update|delete|insert标签里面的值
     *
     * @param sql
     * @param param
     * @return
     */
    @Override
    public String parser(String sql, HashMap<String, Object> param) {
        Matcher m = getMatcher("(?<=>)(\\s|.)*(?=<)", sql);
        if (m.find()) {
            return m.group(0).trim();
        }
        return sql;
    }


    @Override
    public int order() {
        return 0;
    }

}
