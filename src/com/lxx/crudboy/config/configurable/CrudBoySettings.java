package com.lxx.crudboy.config.configurable;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.lxx.crudboy.config.ConfigListenersRegistry;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName CrudBoySetting
 * @Author laixiaoxing
 * @Date 2020/2/13 下午3:58
 * @Description 保存数据库配置
 * @Version 1.0
 */
@State(name = "CrudBoySettings", storages = {@Storage(id = "crud", file = "$APP_CONFIG$/CrudBoy-settings.xml")})
public class CrudBoySettings implements PersistentStateComponent<CrudBoySettings> {


    @Nullable
    @Override
    public CrudBoySettings getState() {
        return this;
    }


    @Override
    public void loadState(CrudBoySettings crudBoySetting) {
        XmlSerializerUtil.copyBean(crudBoySetting, this);
    }


    public CrudBoySettings() {
        loadDefaultSettings();
    }

    private void loadDefaultSettings() {
        this.configResult = new HashMap<>();
        configResult.put("modelPath", "待填写");
        configResult.put("url", "待填写");
        configResult.put("jdbcDriver", "待填写");
        configResult.put("user", "待填写");
        configResult.put("password", "待填写");
        configResult.put("doSuffix", "待填写");
        configResult.put("mybatis", "待填写");
    }


    public Map<String, String> getConfigResult() {
        return this.configResult;
    }

    public String setConfigResult(Map<String, String> configResult) {
        this.configResult = configResult;
        return notifyRegistries();
    }


    private Map<String, String> configResult;

    @Transient
    private List<ConfigListenersRegistry> registries = new ArrayList<>();

    public void registries(List<ConfigListenersRegistry> registry) {
        registries.addAll(registry);
    }


    private String notifyRegistries() {
        for (ConfigListenersRegistry registry : registries) {
            String res= registry.refresh(this);
            if (StringUtils.isNotEmpty(res)){
                return res;
            }
        }
        return StringUtils.EMPTY;
    }

}
