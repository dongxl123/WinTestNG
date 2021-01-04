package com.winbaoxian.testng.core.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.AbstractFreemarkerConfiguration;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 14:31
 */
@Component
@Slf4j
public class ParamsExecutor extends AbstractFreemarkerConfiguration {

    @Resource
    private FreeMarkerConfigurer freemarkerConfig;

    @PostConstruct
    public void initFunction() {
        try {
            super.settings(freemarkerConfig.getConfiguration());
        } catch (Exception e) {
            log.error("freemarker init error", e);
        }
    }

    public String render(String content, TestCasesRunContext context) {
        Map<String, Object> model = new HashMap<>();
        if (context != null) {
            if (MapUtils.isNotEmpty(context.getGlobalParams())) {
                model.putAll(context.getGlobalParams());
            }
        }
        return render(content, model);
    }

    public String render(String content, Map<String, Object> model) {
        if (StringUtils.isBlank(content) || !needRender(content)) {
            return content;
        }
        try {
            //log.info("Before render,{}", content);
            Template template = new Template(null, content, freemarkerConfig.getConfiguration());
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            log.error("ParamsExecutor.render error, tpl:{}, model:{}", content, JsonUtils.INSTANCE.toJSONString(model));
            throw new WinTestNgException(String.format("ParamsExecutor.render error, tpl:%s", content), e);
        }
    }

    private boolean needRender(String content) {
        if (content.contains(WinTestNGConstant.FREEMARKER_VARIABLE_PREFIX) || content.contains(WinTestNGConstant.FREEMARKER_CONDITION_PREFIX)) {
            return true;
        }
        return false;
    }

    public void renderDeep(JSONObject jo, TestCasesRunContext context) {
        if (jo == null) {
            return;
        }
        for (String key : jo.keySet()) {
            if (StringUtils.equalsIgnoreCase(key, "stepList")) {
                continue;
            }
            Object value = jo.get(key);
            if (value instanceof String) {
                jo.put(key, render((String) value, context));
            } else if (value instanceof JSONObject) {
                JSONObject innerJo = (JSONObject) value;
                renderDeep(innerJo, context);
            } else if (value instanceof JSONArray) {
                JSONArray innerJoArray = (JSONArray) value;
                for (Object o : innerJoArray) {
                    if (o instanceof JSONObject) {
                        renderDeep((JSONObject) o, context);
                    }
                }
            }
        }
    }


}
