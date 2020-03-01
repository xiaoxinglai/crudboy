package com.lxx.crudboy.config.configurable;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.Messages;
import com.lxx.crudboy.config.ui.CrudBoyConfiguration;
import com.lxx.crudboy.model.ConfigResult;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * @ClassName CrudBoyConfigrable
 * @Author laixiaoxing
 * @Date 2020/2/12 下午10:44
 * @Description 数据库和资源路径的配置
 * @Version 1.0
 */
public class CrudBoyConfigurable implements SearchableConfigurable {

    private CrudBoyConfiguration configuration;

    private CrudBoySettings settings;


    public CrudBoyConfigurable() {
        settings = ServiceManager.getService(CrudBoySettings.class);
    }


    @NotNull
    @Override
    public String getId() {
        return "plugins.crudConfig";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "crudConfig";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (configuration == null) {
            configuration = new CrudBoyConfiguration(settings);
        }
        return configuration.getMainPane();
    }

    @Override
    public boolean isModified() {
        ConfigResult configResult = configuration.getInput();
        if (configResult.getDoSuffix() != null || configResult.getJdbcDriver() != null
                || configResult.getModelPath() != null || configResult.getPassword() != null
                || configResult.getUrl() != null || configResult.getUser() != null||configResult.getMybatis() != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void apply() throws ConfigurationException {
        Map<String, String> oldMap = settings.getConfigResult();
        ConfigResult configResult = configuration.getInput();
        Map<String, String> map = getMap(configResult);
        String res = settings.setConfigResult(map);
        if (!StringUtils.EMPTY.equals(res)) {
            Messages.showMessageDialog(res, "Reset Fail", null);
            settings.setConfigResult(oldMap);
            configuration.setInput(oldMap);
        }
    }


    @Override
    public void reset() {
        //        if (configuration != null) {
        //            Messages.showMessageDialog("重置成功", "Reset success", null);
        //        }
    }

    @NotNull
    private Map<String, String> getMap(ConfigResult configResult) {
        Map<String, String> map = new HashMap<>();
        map.put("modelPath", configResult.getModelPath());
        map.put("url", configResult.getUrl());
        map.put("jdbcDriver", configResult.getJdbcDriver());
        map.put("user", configResult.getUser());
        map.put("password", configResult.getPassword());
        map.put("doSuffix", configResult.getDoSuffix());
        map.put("mybatis", configResult.getMybatis());
        return map;
    }
}
