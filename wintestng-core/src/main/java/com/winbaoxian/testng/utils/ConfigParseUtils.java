package com.winbaoxian.testng.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.winbaoxian.testng.enums.ActionType;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import com.winbaoxian.testng.model.core.action.LogicActionSetting;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum ConfigParseUtils {

    INSTANCE;

    private Logger log = LoggerFactory.getLogger(ConfigParseUtils.class);

    public List<ActionSetting> parseActionSettingList(String config) {
        if (StringUtils.isBlank(config)) {
            return null;
        }
        List<Object> settingList =  JSON.parseObject(config, List.class,Feature.OrderedField);
        List<ActionSetting> actionSettingList = new ArrayList<>();
        for (Object o : settingList) {
            JSONObject jo = (JSONObject) o;
            ActionSetting actionSetting = parseOneActionSetting(jo);
            if (actionSetting != null) {
                actionSettingList.add(actionSetting);
            }
        }
        return actionSettingList;
    }

    public ActionSetting parseOneActionSetting(JSONObject jo) {
        if (jo == null) {
            return null;
        }
        if (!jo.containsKey("actionType")) {
            log.warn("actionSetting config missing type, {}", jo.toJSONString());
            return null;
        }

        String type = jo.getString("actionType");
        if (StringUtils.isBlank(type)) {
            return null;
        }
        Class<? extends ActionSetting> actionSettingsClass = determineActionSettingClass(ActionType.getActionType(type));
        if (actionSettingsClass == null) {
            log.error("没有找到操作配置对象类,type:{}", type);
            return null;
        }
        try {
            ActionSetting actionSetting = JSON.parseObject(jo.toJSONString(), actionSettingsClass);
            if (actionSetting instanceof LogicActionSetting) {
                LogicActionSetting realActionSetting = (LogicActionSetting) actionSetting;
                if (jo.containsKey("stepList")) {
                    realActionSetting.setStepList(parseActionSettingList(jo.get("stepList").toString()));
                }
            }
            return actionSetting;
        } catch (Exception e) {
            log.error("parseOneActionSetting error, config:{}", jo.toJSONString(), e);
        }
        return null;
    }

    @SneakyThrows
    private Class<? extends ActionSetting> determineActionSettingClass(ActionType type) {
        if (type == null) {
            return null;
        }
        String basePackage = ClassUtils.getPackageCanonicalName(ActionSetting.class);
        if (BooleanUtils.isTrue(type.getLogicFlag())) {
            basePackage += ".logic";
        } else {
            basePackage += ".normal";
        }
        String className = String.format("%s.%sActionSetting", basePackage, StringUtils.capitalize(StringUtils.lowerCase(type.name())));
        Class clazz = ClassUtils.getClass(className);
        if (ClassUtils.isAssignable(clazz, ActionSetting.class)) {
            return clazz;
        }
        return null;
    }

}
