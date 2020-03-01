package com.lxx.crudboy.util;

import com.lxx.crudboy.config.ConfigListenersRegistry;
import com.lxx.crudboy.config.configurable.CrudBoySettings;
import com.lxx.crudboy.util.sqlfilte.impl.*;
import com.lxx.crudboy.util.sqlfilte.SqlParserFilter;
import org.apache.commons.lang3.StringUtils;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author laixiaoxing
 * @Description jdbc配置
 * @Date 下午1:24 2020/2/14
 */
public class MysqlJdbc implements ConfigListenersRegistry<CrudBoySettings> {
    private String className;
    private String jdbcUrl;
    private String user;
    private String password;
    private Connection connection;
    private DatabaseMetaData databaseMetaData;
    private volatile static MysqlJdbc mysqlJdbc;
    private List<SqlParserFilter> parserFilters;

    public static MysqlJdbc getMysqlJdbc(CrudBoySettings settings) {

        if (mysqlJdbc == null) {
            synchronized (MysqlJdbc.class) {
                if (mysqlJdbc == null) {
                    mysqlJdbc = new MysqlJdbc(settings);
                }
            }
            return mysqlJdbc;
        } else {
            return mysqlJdbc;
        }
    }


    public MysqlJdbc(CrudBoySettings settings) {
        List<ConfigListenersRegistry> mysqlJdbcs = new ArrayList<>();
        mysqlJdbcs.add(this);
        settings.registries(mysqlJdbcs);
        init(settings);

        parserFilters = InitParser();
    }

    private List<SqlParserFilter> InitParser() {
        List<SqlParserFilter> parserFilterList = new ArrayList<>();
        DefaulterParser defaulterParser = new DefaulterParser();
        IfParser ifParser = new IfParser();
        ChooseParser chooseParser = new ChooseParser();
        WhereParser whereParser = new WhereParser();
        TrimParser trimParser = new TrimParser();
        SetParser setParser = new SetParser();
        ForeachParser foreachParser = new ForeachParser();

        parserFilterList.add(defaulterParser);
        parserFilterList.add(ifParser);
        parserFilterList.add(chooseParser);
        parserFilterList.add(whereParser);
        parserFilterList.add(trimParser);
        parserFilterList.add(setParser);
        parserFilterList.add(foreachParser);

        parserFilterList = parserFilterList.stream().sorted(((o1, o2) -> o1.order() > o2.order() ? 1 : -1))
                .collect(Collectors.toList());
        return parserFilterList;
    }

    private String init(CrudBoySettings settings) {
        Map<String, String> config = settings.getConfigResult();
        this.className = config.get("jdbcDriver");
        this.jdbcUrl = config.get("url");
        this.user = config.get("user");
        this.password = config.get("password");
        try {
            Class.forName(this.className).newInstance();
            String url = String.format(this.jdbcUrl, this.user, this.password);
            this.connection = DriverManager.getConnection(url);
            this.databaseMetaData = this.connection.getMetaData();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return StringUtils.EMPTY;
    }


    public DatabaseMetaData getDatabaseMetaData() {
        return this.databaseMetaData;
    }

    public String jdbcType(String type) {
        if (type.equals("INT")) {
            return "Integer ";
        }
        if (type.equals("VARCHAR")) {
            return "String ";
        }
        if (type.equals("TIMESTAMP")) {
            return "Date ";
        }
        if (type.equals("TEXT")) {
            return "String ";
        }
        if (type.equals("BIGINT")) {
            return "Long ";
        }
        if (type.equals("DATETIME")) {
            return "Date ";
        }
        if (type.equals("DECIMAL")) {
            return "BigDecimal ";
        }
        return null;
    }

    @Override
    public String refresh(CrudBoySettings settings) {
        return init(settings);
    }


    public String excute(String sql, HashMap<String, Object> param) throws Exception {
        for (SqlParserFilter parserFilter : parserFilters) {
            sql = parserFilter.parser(sql, param);
        }
        for (Map.Entry<String, Object> kv : param.entrySet()) {
            if (!(kv.getValue() instanceof Map)) {
                String value = kv.getValue() == null ? "Null" : "'" + kv.getValue().toString() + "'";
                sql = sql.replaceAll("#\\{" + kv.getKey() + "\\}", value);
            } else {
                if (kv.getValue() instanceof Map) {
                    Map<String, Object> mapParam = (Map) kv.getValue();
                    for (Map.Entry<String, Object> keyAndValue : mapParam.entrySet()) {
                        String value =
                                keyAndValue.getValue() == null ? "Null" : "'" + keyAndValue.getValue().toString() + "'";
                        if (param.get(keyAndValue.getKey()) == null) {
                            sql = sql.replaceAll("#\\{" + keyAndValue.getKey() + "\\}", value);
                        }
                        sql = sql.replaceAll("#\\{" + kv.getKey() + "." + keyAndValue.getKey() + "\\}", value);
                    }
                }
            }
        }

        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet;
        if (sql.trim().startsWith("select") || sql.trim().startsWith("SELECT")) {
            resultSet = ps.executeQuery();
            StringBuilder res = new StringBuilder();
            while (resultSet.next()) {
                res.append(resultSet.getLong("id"));
                res.append(",");
                res.append(resultSet.getString("name"));
                res.append(",");
                res.append(resultSet.getString("sex"));
                res.append("\n");
            }
            return res.toString();
        } else {
            //开启事务
            connection.setAutoCommit(false);
            int a = ps.executeUpdate();
            connection.rollback();//回滚事务
            return "变更条数" + a;
        }

    }


}

