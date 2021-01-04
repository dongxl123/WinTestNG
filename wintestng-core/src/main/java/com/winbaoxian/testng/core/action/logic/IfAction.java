package com.winbaoxian.testng.core.action.logic;

import com.winbaoxian.testng.core.action.ActionExecutor;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.logic.IfActionSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class IfAction extends AbstractLogicAction<IfActionSetting> {

    @Resource
    private ActionExecutor actionExecutor;

    @Override
    protected Object executeInternal(IfActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        List<String> noticeTagList = new ArrayList<>();
        if (StringUtils.isBlank(actionSetting.getCondition())) {
            noticeTagList.add("条件为空");
            return noticeTagList;
        }
        if (actionSetting.getCondition().equalsIgnoreCase(Boolean.TRUE.toString())) {
            noticeTagList.add("条件为true");
            actionExecutor.execute(actionSetting.getStepList(), context);
        } else {
            noticeTagList.add("条件为false");
        }
        return noticeTagList;
    }

}
