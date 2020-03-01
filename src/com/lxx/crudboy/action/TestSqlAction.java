package com.lxx.crudboy.action;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.lxx.crudboy.Enums.CrudBoyEnums;
import com.lxx.crudboy.action.base.BaseSqlAction;
import com.lxx.crudboy.model.ReadResult;
import com.lxx.crudboy.util.AbstractParser;
import groovy.json.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

/**
 * @Author laixiaoxing
 * @Description 测试sql
 * @Date 下午11:51 2020/2/14
 */
public class TestSqlAction extends BaseSqlAction {

    Gson gson = new Gson();

    @Override
    protected void doResult(String res, boolean tag) {
        Messages.showMessageDialog(res, "sql执行结果", null);
    }

    @Override
    protected void getParam(HashMap<String, Object> param, Project project, HashMap<String, String> type) {
        if (param != null) {
            //弹出弹窗来输入参数
            String input = Messages.showMultilineInputDialog(project, "请输入参数", "请输入参数", null, null, null);
            Map map = (Map)JSONObject.parse(input);
            for (Map.Entry<String, Object> kv : param.entrySet()) {
                kv.setValue(map.get(kv.getKey()));
            }
        }
    }


}
