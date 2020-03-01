package com.lxx.crudboy.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


/**
 * @ClassName DataSourceUtil
 * @Author laixiaoxing
 * @Date 2020/2/16 上午12:22
 * @Description 返回mysql数据源配置
 * @Version 1.0
 */
public class DataSourceUtil extends MysqlDataSource {

    public static MysqlDataSource getMysqlDataSource(String url, String user, String password) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(url);
        mysqlDataSource.setUser(user);
        mysqlDataSource.setPassword(password);
        return mysqlDataSource;
    }
}
