package com.lxx.crudboy.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.lxx.crudboy.action.base.BaseAnAction;
import com.lxx.crudboy.util.BaseUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;

/**
 * @Author laixiaoxing
 * @Description //DTO
 * @Date 下午5:39 2020/2/4
 */
public class CreateDTOAction extends BaseAnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        this.init(anActionEvent);
        String doName = Messages.showInputDialog((String)"DO name", (String)"Create DTO", (Icon)Messages.getInformationIcon());
        final PsiElementFactory factory = JavaPsiFacade.getInstance((Project)this.getProject()).getElementFactory();
        HashMap<String, String> param = new HashMap<String, String>();
        String doClassName = BaseUtils.firstLetterUpperCase(
                BaseUtils.markToHump(doName.substring(0, doName.length() - 2), "_", null));
        param.put("doCalssName", doClassName);
        GlobalSearchScope searchScope = GlobalSearchScope.allScope((Project)this.getProject());
        PsiPackage psiPackage = JavaPsiFacade.getInstance((Project)this.getProject()).findPackage("com.lxx.model");
        PsiClass[] doPsiClasss = psiPackage.findClassByShortName(doName, searchScope);
        PsiClass doPsiClass = doPsiClasss[0];
        PsiClass dtoPsiClass = JavaPsiFacade.getInstance((Project)this.getProject()).findClass(doPsiClass.getQualifiedName(), searchScope);
        final PsiField[] psiFields = dtoPsiClass.getFields();
        final PsiClass psiClass = this.getJavaDirectoryService().createClass(this.getPsiDirectory(), "", "MMS_DTO", false, param);
        WriteCommandAction.runWriteCommandAction((Project)this.getProject(), (Runnable)new Runnable(){
            @Override
            public void run() {
                for (PsiField psiField : psiFields) {
                    String comment = psiField.getDocComment().getText().replaceAll("\\*", "").replaceAll("/", "").replaceAll(" ", "").replaceAll("\n", "");
                    StringBuffer fieldStrBuf = new StringBuffer(psiField.getDocComment().getText()).append("\nprivate ").append(psiField.getType().getPresentableText()).append(" ").append(psiField.getName()).append(";");
                    psiClass.add((PsiElement)factory.createFieldFromText(fieldStrBuf.toString(), (PsiElement)psiClass));
                }
            }
        });
    }
}
