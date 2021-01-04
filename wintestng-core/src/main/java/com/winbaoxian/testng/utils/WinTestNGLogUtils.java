package com.winbaoxian.testng.utils;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.AbstractActionSetting;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author dongxuanliang252
 * @date 2019-03-08 15:01
 */
public enum WinTestNGLogUtils {
    INSTANCE;

    public String getActionLogPrefix(ActionSetting currentActionSetting, TestCasesRunContext context) {
        String prefix = getFlowLogPrefix(context);
        if (CollectionUtils.isNotEmpty(context.getActionSettingsStack())) {
            List<String> actionNames = context.getActionSettingsStack().stream().map(v -> {
                AbstractActionSetting o = (AbstractActionSetting) v;
                return String.format(WinTestNGConstant.LOG_PREFIX_ACTION, o.getName(), o.getActionType());
            }).collect(Collectors.toList());
            prefix += WinTestNGConstant.CHAR_RIGHT_ARROW + StringUtils.join(actionNames, WinTestNGConstant.CHAR_RIGHT_ARROW);
        }
        AbstractActionSetting currentActionConfig = (AbstractActionSetting) currentActionSetting;
        prefix += WinTestNGConstant.CHAR_RIGHT_ARROW + String.format(WinTestNGConstant.LOG_PREFIX_ACTION, currentActionConfig.getName(), currentActionConfig.getActionType());
        return prefix;
    }

    public String getTLogPrefix(TestCasesRunContext context) {
        String prefix = String.format(WinTestNGConstant.LOG_PREFIX_UUID, context.getReportDataContext().getReportUuid());
        if (context.getTestCasesDTO() != null) {
            prefix += WinTestNGConstant.CHAR_COLON + String.format(WinTestNGConstant.LOG_PREFIX_TESTCASE, StringUtils.defaultIfBlank(context.getTestCasesDTO().getName(), context.getTestCasesDTO().getId().toString()));
        }
        return prefix;
    }

    public String getFlowLogPrefix(TestCasesRunContext context) {
        String prefix = getTLogPrefix(context);
        if (StringUtils.isNotBlank(context.getFlowShowTitle())) {
            prefix += WinTestNGConstant.CHAR_RIGHT_ARROW + String.format(WinTestNGConstant.LOG_PREFIX_FLOW, context.getFlowShowTitle());
        }
        return prefix;
    }

    public String[] getTestReportTitles(ActionSetting currentActionSetting, TestCasesRunContext context) {
        List<String> titles = new ArrayList<>();
        Stack<ActionSetting> templateNameStack = context.getActionSettingsStack();
        if (CollectionUtils.isNotEmpty(templateNameStack)) {
            List<String> actionNames = context.getActionSettingsStack().stream().map(
                    v -> {
                        AbstractActionSetting o = (AbstractActionSetting) v;
                        return String.format(WinTestNGConstant.LOG_PREFIX_ACTION, o.getName(), o.getActionType());
                    }).collect(Collectors.toList());
            titles.addAll(actionNames);
        }
        AbstractActionSetting currentActionConfig = (AbstractActionSetting) currentActionSetting;
        titles.add(String.format(WinTestNGConstant.LOG_PREFIX_ACTION, currentActionConfig.getName(), currentActionConfig.getActionType()));
        return titles.toArray(new String[titles.size()]);
    }

    public String getErrorString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }


}
