package com.winbaoxian.testng.core.action.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.winbaoxian.testng.core.action.ActionExecutor;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.logic.ForActionSetting;
import com.winbaoxian.testng.utils.WinTestNGCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ForAction extends AbstractLogicAction<ForActionSetting> {

    @Resource
    private ActionExecutor actionExecutor;

    @Override
    protected Object executeInternal(ForActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        List<String> noticeTagList = new ArrayList<>();
        if (StringUtils.isBlank(actionSetting.getIterData()) || StringUtils.isBlank(actionSetting.getIterAlias())) {
            noticeTagList.add("缺少配置");
            return noticeTagList;
        }
        JSONArray list = JSON.parseArray(actionSetting.getIterData());
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Object> globalParams = context.getGlobalParams();
            for (Object o : list) {
                context.setGlobalParams(new HashMap<>(globalParams));
                context.addGlobalParams(actionSetting.getIterAlias(), o);
                actionExecutor.execute(actionSetting.getStepList(), context);
            }
        }
        noticeTagList.add(String.format("循环%s次", WinTestNGCommonUtils.INSTANCE.getCollectionSize(list)));
        return noticeTagList;
    }

}
