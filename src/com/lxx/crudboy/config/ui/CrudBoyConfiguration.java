package com.lxx.crudboy.config.ui;

import com.intellij.openapi.components.ServiceManager;
import com.lxx.crudboy.config.configurable.CrudBoySettings;
import com.lxx.crudboy.model.ConfigResult;

import javax.swing.*;
import java.util.Map;

/**
 * @ClassName CrudBoyConfiguration
 * @Author laixiaoxing
 * @Date 2020/2/12 下午10:54
 * @Description 配置面板 负责配置数据库资源和DO路径
 * @Version 1.0
 */
public class CrudBoyConfiguration {
    /**
     * 主面板
     */
    private JPanel mainPane;
    private JTextField modelPath;
    private JTextField url;
    private JTextField jdbcDriver;
    private JTextField user;
    private JTextField password;
    private JTextField doSuffix;
    private JTextField mybatis;


    public CrudBoyConfiguration(CrudBoySettings setting) {
        Map<String,String> configResult = setting.getConfigResult();
        initInput(configResult);
    }

    private void initInput(Map<String, String> configResult) {
        modelPath.setText(configResult.get("modelPath"));
        url.setText(configResult.get("url"));
        jdbcDriver.setText(configResult.get("jdbcDriver"));
        user.setText(configResult.get("user"));
        password.setText(configResult.get("password"));
        doSuffix.setText(configResult.get("doSuffix"));
        //mybatis.setText(configResult.get("mybatis"));
    }


    /**
     * 获取主面板
     *
     * @return
     */
    public JPanel getMainPane() {
        return mainPane;
    }


    public ConfigResult getInput() {
        return new ConfigResult(modelPath.getText(), url.getText(), jdbcDriver.getText(), user.getText(),
                password.getText(), doSuffix.getText());
    }


    public void setInput(Map<String, String> configResult) {
        initInput(configResult);
    }
}
