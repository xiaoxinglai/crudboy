package com.lxx.crudboy.model;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;

import java.util.List;

/**
 * @ClassName MapResult
 * @Author laixiaoxing
 * @Date 2020/2/11 下午9:41
 * @Description 表字段转换结果
 * @Version 1.0
 */
public class MapResult {

    private String sqlText;

    private List<XmlTag> attributes;


    public MapResult(String sqlText, List<XmlTag> attributes) {
        this.sqlText = sqlText;
        this.attributes = attributes;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<XmlTag> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<XmlTag> attributes) {
        this.attributes = attributes;
    }
}
