package com.lxx.crudboy.action.base;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.lxx.crudboy.Enums.CrudBoyEnums;
import com.lxx.crudboy.util.MysqlJdbc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author laixiaoxing
 * @Description 测试sql
 * @Date 下午11:51 2020/2/14
 */
public abstract class BaseSqlAction extends BaseAnAction {


    protected AnActionEvent e;

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.e = e;
        this.init(e);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //获取选中的类名
        String name = psiFile.getVirtualFile().getName();
        //获取选中的方法名
        //获取鼠标所在的元素
        PsiElement psiElement = e.getData(PlatformDataKeys.PSI_ELEMENT);
        String method = psiElement.toString().replace("PsiMethod:", "");
        name = name.replace(".java", ".xml");

        //获取方法的参数
        PsiParameter[] psiParameters = ((PsiMethodImpl) psiElement).getParameterList().getParameters();
        //name-value
        HashMap<String, Object> param = null;
        //name-type
        HashMap<String, String> type = null;
        if (psiParameters.length != 0) {
            param = new HashMap<>(psiParameters.length);
            type = new HashMap<>(psiParameters.length);
            for (PsiParameter psiParameter : psiParameters) {
                param.put(psiParameter.getName(), null);
                type.put(psiParameter.getName(), psiParameter.getType().toString().replace("PsiType:", ""));
            }
        }

        Project project = e.getData(PlatformDataKeys.PROJECT);
        getParam(param, project, type);
        //获取该类名对应的xml文件
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, name, GlobalSearchScope.projectScope(project));
        //获取xml文件中和方法名对应的sql
        XmlTag  target = getXmlTag(method, psiFiles[0]);


        String content = ((XmlTagImpl) target).getText();
        MysqlJdbc jdbc = getMysqlJdbc();
        try {
            String res = jdbc.excute(content, param);
            doResult(res, true);
        } catch (Exception e1) {
            doResult(e1.getMessage(), false);
        }
    }

    private XmlTag getXmlTag(String method, PsiFile psiFile) {
        XmlTag[] xmlTag = ((XmlFile) psiFile).getRootTag().findSubTags("select");
        for (XmlTag tag : xmlTag) {
            if (tag.getAttribute("id").getValue().equals(method)) {
                return tag;
            }
        }


        xmlTag = ((XmlFile) psiFile).getRootTag().findSubTags("insert");
        for (XmlTag tag : xmlTag) {
            if (tag.getAttribute("id").getValue().equals(method)) {
                return tag;
            }
        }


        xmlTag = ((XmlFile) psiFile).getRootTag().findSubTags("update");
        for (XmlTag tag : xmlTag) {
            if (tag.getAttribute("id").getValue().equals(method)) {
                return tag;
            }
        }


        xmlTag = ((XmlFile) psiFile).getRootTag().findSubTags("delete");
        for (XmlTag tag : xmlTag) {
            if (tag.getAttribute("id").getValue().equals(method)) {
                return tag;
            }
        }
        return null;
    }


    /**
     * 处理返回结果
     *
     * @param res
     * @param tag
     */
    protected abstract void doResult(String res, boolean tag);

    /**
     * 获取参数
     *
     * @param param
     * @param project
     * @param type
     */
    protected abstract void getParam(HashMap<String, Object> param, Project project, HashMap<String, String> type);


    /**
     * 获取正则匹配
     *
     * @param patter
     * @param text
     * @return
     */
    protected Matcher getMatcher(String patter, String text) {
        Pattern pattern = Pattern.compile(patter, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(text);
    }
}
