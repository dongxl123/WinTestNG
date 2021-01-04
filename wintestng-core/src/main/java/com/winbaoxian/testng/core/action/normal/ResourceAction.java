package com.winbaoxian.testng.core.action.normal;

import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.core.resource.ResourceExecutor;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.ResourceActionSetting;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 14:21
 */
@Component
public class ResourceAction implements IAction<ResourceActionSetting> {

    @Resource
    private ResourceExecutor resourceExecutor;

    @Override
    public Object execute(ResourceActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        return resourceExecutor.executeDbQuery(actionSetting);
    }
}
