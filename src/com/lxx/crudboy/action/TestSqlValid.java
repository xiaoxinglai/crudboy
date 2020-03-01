package com.lxx.crudboy.action;

import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.lxx.crudboy.action.base.BaseSqlAction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @Author laixiaoxing
 * @Description 测试sql是否有效
 * @Date 下午9:56 2020/2/15
 */
public class TestSqlValid extends BaseSqlAction {

    @Override
    protected void doResult(String res, boolean tag) {
        if (tag) {
            Messages.showMessageDialog("sql有效，执行成功", "sql校验结果", null);
        } else {
            Messages.showMessageDialog(res, "sql校验结果", null);
        }

    }

    @Override
    protected void getParam(HashMap<String, Object> param, Project project, HashMap<String, String> type) {
        if (param != null) {
            for (Map.Entry<String, String> kv : type.entrySet()) {
                if (type.get(kv.getKey()) != null) {
                    param.put(kv.getKey(), convertType(kv.getValue()));
                }
            }
        }
    }

    private Object convertType(String value) {
        Object res = "";
        switch (value) {
            case "Long":
                res = "1";
                break;
            case "String":
                res = "test";
                break;
            case "Int":
                res = "1";
                break;
            case "List<String>":
                res = Arrays.asList("a");
            default:
                Project project = e.getData(PlatformDataKeys.PROJECT);
                PsiFile[] psiFiles = FilenameIndex
                        .getFilesByName(project, value + ".java", GlobalSearchScope.projectScope(project));
                for (PsiFile psiFile : psiFiles) {
                    PsiJavaFile javaFile = (PsiJavaFile) psiFile;
                    Matcher matcher = getMatcher("(?<=private).*(?=;)", javaFile.getText());
                    HashMap<String, Object> objectHashMap = new HashMap();
                    while (matcher.find()) {
                        String filed = matcher.group(0).trim();
                        String[] strings = filed.split(" ");
                        objectHashMap.put(strings[1].trim(),convertType(strings[0].trim()));
                    }
                    return objectHashMap;
                }
        }
        return res;
    }
}
