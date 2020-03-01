package com.lxx.crudboy.action;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.lxx.crudboy.action.base.BaseAnAction;
import com.lxx.crudboy.util.BaseUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CreateServiceAction
 * @Author laixiaoxing
 * @Date 2020/2/2 下午2:28
 * @Description 创建server和实现类
 * @Version 1.0
 */
public class CreateServiceAction extends BaseAnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        this.init(anActionEvent);
        String serviceName = Messages.showInputDialog((String)"Service name", (String)"Create service", (Icon)Messages.getInformationIcon());
        Map<String, String> param = new HashMap<String, String>();
        param.put("doCalssName", BaseUtils.firstLetterUpperCase(BaseUtils.markToHump(serviceName, "_", null)));
        param.put("tableName", serviceName);
        PsiDirectory implDir = this.getPsiDirectory().findSubdirectory("impl");
        if (implDir == null) {
            implDir = this.getPsiDirectory().createSubdirectory("impl");
        }
        PsiClass servicePsiClass = this.getJavaDirectoryService().createClass(this.getPsiDirectory(), "", "MMS_Service", false, param);
        String packagePath = servicePsiClass.getQualifiedName();
        String implT = "impl";
        param.put(implT, packagePath + ";");
        param.put("serviceName", serviceName);
        this.getJavaDirectoryService().createClass(implDir, "", "MMS_ServiceImpl", false, param);
    }
}