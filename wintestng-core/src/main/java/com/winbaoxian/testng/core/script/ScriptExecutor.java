package com.winbaoxian.testng.core.script;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.testng.enums.ScriptLang;
import com.winbaoxian.testng.model.core.action.normal.ScriptActionSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2020-07-20 14:37
 */
@Component
@Slf4j
public class ScriptExecutor {

    public Object execute(ScriptActionSetting setting) {
        if (setting.getLang() == null || StringUtils.isBlank(setting.getContent())) {
            return null;
        }
        IScriptLangRunner runner = determineScriptLangRunner(setting.getLang());
        if (runner == null) {
            log.info("ScriptExecutor can not found runner, {}", JSON.toJSONString(setting.getContent()));
            return null;
        }
        return runner.execute(setting);
    }


    @Resource
    private ApplicationContext applicationContext;

    private IScriptLangRunner determineScriptLangRunner(ScriptLang lang) {
        if (lang == null) {
            return null;
        }
        String beanName = String.format("%sRunner", StringUtils.lowerCase(lang.name()));
        return applicationContext.getBean(beanName, IScriptLangRunner.class);
    }

}
