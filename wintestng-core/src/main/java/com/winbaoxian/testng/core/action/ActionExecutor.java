package com.winbaoxian.testng.core.action;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.enums.ActionType;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.AbstractActionSetting;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import com.winbaoxian.testng.model.core.action.LogicActionSetting;
import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.utils.ConfigParseUtils;
import com.winbaoxian.testng.utils.ConsoleLogUtils;
import com.winbaoxian.testng.utils.CopyUtils;
import com.winbaoxian.testng.utils.WinTestNGLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 14:28
 */
@Component
@Slf4j
public class ActionExecutor {

    @Resource
    private ParamsExecutor paramsExecutor;

    public void execute(List<ActionSetting> actionSettingList, TestCasesRunContext context) throws Exception {
        if (CollectionUtils.isEmpty(actionSettingList)) {
            return;
        }
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        for (ActionSetting actionSetting : actionSettingList) {
            AbstractActionSetting actionConfig = (AbstractActionSetting) actionSetting;
            String tFlowActionLogPrefix = WinTestNGLogUtils.INSTANCE.getActionLogPrefix(actionConfig, context);
            String[] testReportTitles = WinTestNGLogUtils.INSTANCE.getTestReportTitles(actionConfig, context);
            if (BooleanUtils.isTrue(actionConfig.getDisableFlag())) {
                log.info("[{}] action disabled", tFlowActionLogPrefix);
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]action disabled", tFlowActionLogPrefix));
                }
                continue;
            }
            log.info("[{}]execute action start", tFlowActionLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]execute action start", tFlowActionLogPrefix));
            }
            reportDataContext.logText("开始执行", testReportTitles);
            Object ret = null;
            try {
                ret = execute(actionConfig, context);
            } catch (Exception e) {
                reportDataContext.logException(e, testReportTitles);
                throw e;
            }
            //记录noticeMsg
            logActionNoticeMsg(actionConfig, context, ret);
            if (BooleanUtils.isTrue(actionConfig.getActionType().getLogicFlag())) {
                //逻辑步骤
                log.info("[{}]execute action end", tFlowActionLogPrefix);
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]execute action end", tFlowActionLogPrefix));
                }
                reportDataContext.logText(String.format("执行完成"), testReportTitles);
            } else {
                //普通步骤
                String alias = ((NormalActionSetting) actionSetting).getAlias();
                if (StringUtils.isNotBlank(alias)) {
                    context.addGlobalParams(alias, ret);
                }
                log.info("[{}]execute action end, alias:{}, return:{}", tFlowActionLogPrefix, alias, JsonUtils.INSTANCE.toJSONString(ret));
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]execute action end, alias:%s, return:%s", tFlowActionLogPrefix, alias, JsonUtils.INSTANCE.toJSONString(ret)));
                }
                reportDataContext.logText(String.format("执行完成, 别名:%s, 数据:%s", alias, JsonUtils.INSTANCE.toPrettyJSONString(ret)), testReportTitles);
            }
            Long delayTimes = actionConfig.getDelayTimes();
            if (delayTimes != null && delayTimes > 0) {
                Thread.sleep(actionConfig.getDelayTimes());
                log.info("[{}]after execute action, delay times:{}ms", tFlowActionLogPrefix, delayTimes);
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]after execute action, delay times:%sms", tFlowActionLogPrefix, delayTimes));
                }
                reportDataContext.logText(String.format("延迟, delay times:%s(ms)", delayTimes), testReportTitles);
            }
            //设置结束标识
            reportDataContext.setStepLogEndFlag(testReportTitles);
        }
    }

    private Object execute(AbstractActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        if (actionSetting == null) {
            return null;
        }
        //参数替换
        String tFlowActionLogPrefix = WinTestNGLogUtils.INSTANCE.getActionLogPrefix(actionSetting, context);
        String[] testReportTitles = WinTestNGLogUtils.INSTANCE.getTestReportTitles(actionSetting, context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        ActionType actionType = actionSetting.getActionType();
        //TPL替换
        JSONObject actionJo = (JSONObject) JsonUtils.INSTANCE.toJSON(actionSetting);
        //模板渲染前LOG
        JSONObject logActionJo = null;
        if (BooleanUtils.isTrue(actionSetting.getActionType().getLogicFlag())) {
            logActionJo = CopyUtils.INSTANCE.deepClone(actionJo);
            logActionJo.remove("stepList");
        } else {
            logActionJo = actionJo;
        }
        log.info("[{}]Action配置(模板):{}", tFlowActionLogPrefix, JsonUtils.INSTANCE.toJSONString(logActionJo));
        if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]Action配置(模板):%s", tFlowActionLogPrefix, JsonUtils.INSTANCE.toJSONString(logActionJo)));
        }
        reportDataContext.logText(String.format("Action配置(模板):%s", JsonUtils.INSTANCE.toJSONString(logActionJo)), testReportTitles);
        paramsExecutor.renderDeep(actionJo, context);
        //模板渲染后LOG
        if (BooleanUtils.isTrue(actionSetting.getActionType().getLogicFlag())) {
            logActionJo = CopyUtils.INSTANCE.deepClone(actionJo);
            logActionJo.remove("stepList");
        } else {
            logActionJo = actionJo;
        }
        log.info("[{}]Action配置(结果):{}", tFlowActionLogPrefix, JsonUtils.INSTANCE.toJSONString(logActionJo));
        if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]Action配置(结果):%s", tFlowActionLogPrefix, JsonUtils.INSTANCE.toPrettyJSONString(logActionJo)));
        }
        reportDataContext.logText(String.format("Action配置(结果):%s", JsonUtils.INSTANCE.toPrettyJSONString(logActionJo)), testReportTitles);
        ActionSetting newActionSetting = ConfigParseUtils.INSTANCE.parseOneActionSetting(actionJo);
        IAction iAction = determineAction(actionType);
        if (iAction == null) {
            log.info("没有找到操作执行器, type:{}", actionType);
            throw new WinTestNgException(String.format("没有找到操作执行器,type:%s", actionType));
        }
        return iAction.execute(newActionSetting, context);
    }

    @Resource
    private ApplicationContext applicationContext;

    private IAction determineAction(ActionType type) {
        if (type == null) {
            return null;
        }
        String beanName = String.format("%sAction", StringUtils.lowerCase(type.name()));
        return applicationContext.getBean(beanName, IAction.class);
    }

    private void logActionNoticeMsg(ActionSetting actionSetting, TestCasesRunContext context, Object ret) {
        String[] testReportTitles = WinTestNGLogUtils.INSTANCE.getTestReportTitles(actionSetting, context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        List<String> noticeTagList = new ArrayList<>();
        if (actionSetting instanceof NormalActionSetting) {
            NormalActionSetting actionConfig = (NormalActionSetting) actionSetting;
            if (StringUtils.isNotBlank(actionConfig.getAlias()) && !ActionType.ASSERTION.equals(actionConfig.getActionType())) {
                noticeTagList.add(actionConfig.getAlias());
                if (ret == null) {
                    noticeTagList.add("返回null");
                } else if ((ret instanceof Map) && MapUtils.isEmpty((Map) ret)) {
                    noticeTagList.add("返回空");
                } else if ((ret instanceof Collection) && CollectionUtils.isEmpty((Collection) ret)) {
                    noticeTagList.add("返回空");
                } else if (ActionType.HTTP.equals(actionConfig.getActionType())) {
                    if ((ret instanceof Map) && ((Map) ret).containsKey("statusCode")) {
                        noticeTagList.add(JsonUtils.INSTANCE.toJSONString(((Map) ret).get("statusCode")));
                    }
                }
            }
        } else if (actionSetting instanceof LogicActionSetting) {
            if (ret instanceof List) {
                noticeTagList = (List<String>) ret;
            }
        }
        reportDataContext.logNoticeMsg(StringUtils.join(noticeTagList, WinTestNGConstant.CHAR_COMMA), testReportTitles);
    }

}
