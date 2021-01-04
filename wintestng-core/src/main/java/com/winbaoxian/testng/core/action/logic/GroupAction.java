package com.winbaoxian.testng.core.action.logic;

import com.winbaoxian.testng.core.action.ActionExecutor;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.logic.GroupActionSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GroupAction extends AbstractLogicAction<GroupActionSetting> {

    @Resource
    private ActionExecutor actionExecutor;

    @Override
    protected Object executeInternal(GroupActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        List<String> noticeTagList = new ArrayList<>();
        if (CollectionUtils.isEmpty(actionSetting.getStepList())) {
            noticeTagList.add("无操作");
            return noticeTagList;
        }
        actionExecutor.execute(actionSetting.getStepList(), context);
        return null;
    }

}
