package com.lxx.crudboy.action;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @Author laixiaoxing
 * @Description 应用管理
 * @Date 下午2:26 2020/2/2
 */
public class CodeComponent implements ApplicationComponent {
    @Override
    public void initComponent() {

    }



    @Override
    public void disposeComponent() {
    }



    @Override
    @NotNull
    public String getComponentName() {
        if ("CreateCrudProjectComponent" == null) {
            CodeComponent.reportNull(0);
        }
        return "CreateCrudProjectComponent";
    }

    private static  void reportNull(int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/lxx/crudboy/action/CodeComponent", "getComponentName"));
    }

}
