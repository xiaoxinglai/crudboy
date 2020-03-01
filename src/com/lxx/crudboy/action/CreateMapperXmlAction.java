package com.lxx.crudboy.action;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.lxx.crudboy.action.base.BaseAnAction;
import com.lxx.crudboy.config.configurable.CrudBoySettings;
import com.lxx.crudboy.model.MapResult;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @ClassName DOMapperAction
 * @Author laixiaoxing
 * @Date 2020/2/2 下午11:51
 * @Description DOMapper生成
 * @Version 1.0
 */
public class CreateMapperXmlAction extends BaseAnAction {
    private CrudBoySettings settings;

    public CreateMapperXmlAction() {
        settings = ServiceManager.getService(CrudBoySettings.class);

    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //初始化
        this.init(anActionEvent);
        //弹出输入框  输入Mapper的名字
        String mapperName = Messages.showInputDialog((String) "Mapper name", (String) "Create Mapper",
                (Icon) Messages.getInformationIcon());
        FileTemplate template = FileTemplateManager.getInstance(getProject()).getInternalTemplate("sqlmap");


        Properties param = new Properties();
        param.put("mapperExtPackage", "com.lxx.model");
        param.put("className", "Student");
        param.put("extMapperSuffix", "DO");

        //获取目录
        PsiDirectory psiDirectory = this.getPsiDirectory();

        XmlElementFactory factory = XmlElementFactoryImpl.getInstance((Project) this.getProject());
        try {
            final PsiElement psiXml = FileTemplateUtil
                    .createFromTemplate(template, mapperName + ".xml", param, psiDirectory);

            XmlElement mapper = (XmlElement) psiXml;
            XmlElement xmlRoot = ((XmlFileImpl) mapper).getDocument().getRootTag();
            XmlTag resultMap = factory.createTagFromText("<resultMap></resultMap>");
            XmlAttribute attribute1 = factory.createXmlAttribute("id", "BaseResultMap");
            resultMap.add(attribute1);
            XmlAttribute attribute3 = factory
                    .createXmlAttribute("type", "com.souche.hyper.ledger.model.dao.bean.CashFlowDetail");
            resultMap.add(attribute3);

            MapResult result = getBaseColumnList(mapperName, factory);
            XmlTag sqlTag = factory.createTagFromText("<sql>" + result.getSqlText() + "</sql>");
            XmlAttribute sqlAttr = factory.createXmlAttribute("id", "Base_Column_List");
            sqlTag.add(sqlAttr);

            WriteCommandAction.runWriteCommandAction((Project) this.getProject(), (Runnable) new Runnable() {
                @Override
                public void run() {
                    for (XmlTag xmlTag : result.getAttributes()) {
                        resultMap.add(xmlTag);
                    }
                    xmlRoot.add(resultMap);
                    xmlRoot.add(sqlTag);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private MapResult getBaseColumnList(String tableName, XmlElementFactory factory) {
        try {
            String percentSignMark = "%";
            String spilt = ",";
            String str = null;
            ResultSet tableRet = this.getMysqlJdbc().getDatabaseMetaData()
                    .getColumns(str, percentSignMark, tableName, percentSignMark);
            ArrayList<XmlTag> results = new ArrayList<>();
            StringBuilder baseColumnList = new StringBuilder();
            while (tableRet.next()) {
                String fieldName = tableRet.getString("COLUMN_NAME");
                baseColumnList.append(fieldName).append(",");
                XmlTag result = factory.createTagFromText("<result/>");
                XmlAttribute resultColumn = factory.createXmlAttribute("column", fieldName);
                result.add(resultColumn);
                XmlAttribute resultJdbc = factory
                        .createXmlAttribute("jdbcType", this.getMysqlJdbc().jdbcType(tableRet.getString("TYPE_NAME")));
                result.add(resultJdbc);
                results.add(result);
            }
            String res = baseColumnList.toString();
            if (res.endsWith(spilt)) {
                res = res.substring(0, res.lastIndexOf(","));
            }
            return new MapResult(res, results);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
