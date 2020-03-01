package com.lxx.crudboy.util.sqlfilte;

import java.util.HashMap;

public interface SqlParserFilter {

    /**
     * 解析sql
     * @param sql
     * @param param
     * @return
     */
    String parser(String sql,HashMap<String,Object> param) throws Exception;

    /**
     * 顺序
     * @return
     */
    int order();
}
