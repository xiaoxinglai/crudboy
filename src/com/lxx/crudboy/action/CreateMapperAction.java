package com.lxx.crudboy.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.lxx.crudboy.action.base.BaseAnAction;
import com.lxx.crudboy.util.BaseUtils;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DOMapperAction
 * @Author laixiaoxing
 * @Date 2020/2/2 下午11:51
 * @Description DOMapper生成
 * @Version 1.0
 */
public class CreateMapperAction extends BaseAnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //初始化
        this.init(anActionEvent);
        //弹出输入框  输入Mapper的名字
        String mapperName = Messages.showInputDialog((String) "Mapper name", (String) "Create Mapper",
                (Icon) Messages.getInformationIcon());
        //模版上要用到的参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("doCalssName", BaseUtils.firstLetterUpperCase(BaseUtils.markToHump(mapperName, "_", null)));
        //根据模版 在文件夹中创建类
        this.getJavaDirectoryService().createClass(this.getPsiDirectory(), "", "MMS_Mapper", false, param);
    }
}
