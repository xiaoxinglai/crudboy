package com.lxx.crudboy.model;


import java.io.Serializable;

/**
 * @ClassName MapResult
 * @Author laixiaoxing
 * @Date 2020/2/11 下午9:41
 * @Description 表字段转换结果
 * @Version 1.0
 */
public class ConfigResult  implements Serializable{

    static final long serialVersionUID = 3952882688968447265L;

    private String modelPath;
    private String url;
    private String jdbcDriver;
    private String user;
    private String password;
    private String doSuffix;
    private String mybatis;

    public ConfigResult(String modelPath, String url, String jdbcDriver, String user, String password,
            String doSuffix,String mybatis) {
        this.modelPath = modelPath;
        this.url = url;
        this.jdbcDriver = jdbcDriver;
        this.user = user;
        this.password = password;
        this.doSuffix = doSuffix;
        this.mybatis=mybatis;
    }

    public ConfigResult(String modelPath, String url, String jdbcDriver, String user, String password,
            String doSuffix) {
        this.modelPath = modelPath;
        this.url = url;
        this.jdbcDriver = jdbcDriver;
        this.user = user;
        this.password = password;
        this.doSuffix = doSuffix;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDoSuffix(String doSuffix) {
        this.doSuffix = doSuffix;
    }

    public String getModelPath() {
        return modelPath;
    }

    public String getUrl() {
        return url;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDoSuffix() {
        return doSuffix;
    }

    public String getMybatis() {
        return mybatis;
    }

    public void setMybatis(String mybatis) {
        this.mybatis = mybatis;
    }

    @Override
    public String toString() {
        return "ConfigResult{" + "modelPath='" + modelPath + '\'' + ", url='" + url + '\'' + ", jdbcDriver='"
                + jdbcDriver + '\'' + ", user='" + user + '\'' + ", password='" + password + '\'' + ", doSuffix='"
                + doSuffix + '\'' + ", mybatis='" + mybatis + '\'' + '}';
    }
}
