package com.winbaoxian.testng.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.core.action.ActionExecutor;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.core.data.DataPreparationExecutor;
import com.winbaoxian.testng.enums.ActionType;
import com.winbaoxian.testng.enums.TriggerMode;
import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import com.winbaoxian.testng.model.core.action.normal.TplActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.service.AnalysisService;
import com.winbaoxian.testng.service.CacheService;
import com.winbaoxian.testng.service.WinTestNGService;
import com.winbaoxian.testng.utils.ConsoleLogUtils;
import com.winbaoxian.testng.utils.DateFormatUtils;
import com.winbaoxian.testng.utils.WinTestNGLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.winbaoxian.testng.constant.WinTestNGConstant.*;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 17:20
 */

@Service
@Slf4j
public class WinTestNGExecutor {

    @Resource
    private WinTestNGService winTestNGService;
    @Resource
    private ActionExecutor actionExecutor;
    @Resource
    private DataPreparationExecutor dataPreparationExecutor;
    @Resource
    private ParamsExecutor paramsExecutor;
    @Resource
    private CacheService cacheService;
    @Resource
    private AnalysisService analysisService;
    @Value("${project.reportDomain:https://testplatform-api.winbaoxian.cn}")
    private String reportDomain;

    public TestCasesDTO[] getTestCasesList(String sql) {
        return winTestNGService.getTestCasesList(sql);
    }

    /**
     * 执行某个测试计划
     *
     * @param testCasesDTO
     * @param reportDataContext
     * @throws Exception
     */
    public void executeTestCase(TestCasesDTO testCasesDTO, TestReportDataTestCaseDTO reportDataContext) throws Exception, AssertionError {
        log.info("WinTestNG New TestCase");
        ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), "WinTestNG New TestCase");
        if (testCasesDTO == null) {
            return;
        }
        reportDataContext.setStartTime(new Date());
        reportDataContext.setTestCasesId(testCasesDTO.getId());
        TestCasesRunContext context = new TestCasesRunContext(testCasesDTO);
        context.setReportDataContext(reportDataContext);
        try {
            executeTestFlow(context);
        } catch (Throwable e) {
            ConsoleLogUtils.INSTANCE.logE(reportDataContext.getReportUuid(), e);
            throw e;
        } finally {
            String logTextTitle = String.format("[%s]", WinTestNGLogUtils.INSTANCE.getTLogPrefix(context));
            reportDataContext.setEndTime(new Date());
            reportDataContext.setCurrentTitle(LOG_TITLE_END);
            String logText = String.format("执行完成, 开始时间:%s, 结束时间:%s, 花费:%.2f(s)", DateFormatUtils.INSTANCE.format(reportDataContext.getStartTime()), DateFormatUtils.INSTANCE.format(reportDataContext.getEndTime()), reportDataContext.getDuration() / 1000.0);
            log.info(logTextTitle + logText);
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), logTextTitle + logText);
            reportDataContext.logText(logText);
            if (BooleanUtils.isTrue(reportDataContext.getAnalysisFlag())) {
                analysisService.analysisTestCasesQualityScore(context);
            }
        }
    }

    private void executeTestFlow(TestCasesRunContext context) throws Exception, AssertionError {
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestCasesDTO testCasesDTO = context.getTestCasesDTO();
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        log.info("▷▷▷▷[{}]executeTestCase start, config: {}", tLogPrefix, JsonUtils.INSTANCE.toJSONString(testCasesDTO));
        if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("▷▷▷▷[%s]executeTestCase start, config: %s", tLogPrefix, JsonUtils.INSTANCE.toJSONString(testCasesDTO)));
        } else {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("▷▷▷▷executeTestCase start, %s", tLogPrefix));
        }
        reportDataContext.setCurrentTitle(LOG_TITLE_START).logText(String.format("测试任务配置:%s", JsonUtils.INSTANCE.toPrettyJSONString(testCasesDTO)));
        //全局参数配置
        reportDataContext.setCurrentTitle(LOG_TITLE_GLOBAL_PARAMS_CONFIG);
        globalParamsConfig(context);
        //基础数据配置
        reportDataContext.setCurrentTitle(LOG_TITLE_BASE_PARAMS_CONFIG);
        baseParamsConfig(context);
        //testCase-前置动作
        reportDataContext.setCurrentTitle(LOG_TITLE_PRE_ACTIONS);
        preActions(context);
        //testCase-获取准备的数据
        reportDataContext.setCurrentTitle(LOG_TITLE_DATA_PREPARATION);
        List<Map<String, Object>> dataList = getPreparationData(context);
        //根据准备的数据执行多遍
        if (CollectionUtils.isNotEmpty(dataList)) {
            Map<String, Object> globalParams = context.getGlobalParams();
            reportDataContext.setTotalCount(dataList.size());
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> data = dataList.get(i);
                //如果是多线程的话，该代码需要修改，线程不安全
                context.setGlobalParams(new HashMap<>(globalParams));
                processPreparationData(context, data);
                //testCase-处理准备的数据
                String configShowTitle = context.getTestCasesDTO().getDataPreparationConfig().getShowTitle();
                String showTitle = null;
                if (StringUtils.isNotBlank(configShowTitle)) {
                    try {
                        showTitle = paramsExecutor.render(configShowTitle, context);
                    } catch (Exception e) {
                        log.error("paramsExecutor.render error,{}", configShowTitle, e);
                    }
                }
                if (StringUtils.isBlank(showTitle)) {
                    showTitle = String.format("第%d次", i + 1);
                }
                context.setFlowShowTitle(showTitle);
                String tFlowLogPrefix = WinTestNGLogUtils.INSTANCE.getFlowLogPrefix(context);
                log.info("▷▷[{}] begin, data:{}", tFlowLogPrefix, JsonUtils.INSTANCE.toJSONString(data));
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("▷▷[%s] begin, data:%s", tFlowLogPrefix, JsonUtils.INSTANCE.toJSONString(data)));
                } else {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("▷▷[%s] begin", showTitle));
                }
                reportDataContext.setCurrentTitle(String.format("%s(%s)", LOG_TITLE_MAIN_ACTIONS, showTitle));
                reportDataContext.logText(JsonUtils.INSTANCE.toPrettyJSONString(data), LOG_SUB_TITLE_ACTIONS_FETCH_TEST_CASE);
                //执行测试流程
                mainActions(context);
                log.info("□□[{}] end, data:{}", tFlowLogPrefix, JsonUtils.INSTANCE.toJSONString(data));
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("□□[%s] end, data:%s", tFlowLogPrefix, JsonUtils.INSTANCE.toJSONString(data)));
                }
            }
        } else {
            reportDataContext.setTotalCount(1);
            //执行测试流
            reportDataContext.setCurrentTitle(LOG_TITLE_MAIN_ACTIONS);
            mainActions(context);
        }
        //mainActions异常时，不处理，在这里处理
        if (CollectionUtils.isNotEmpty(reportDataContext.getExceptions())) {
            throw reportDataContext.getExceptions().get(0);
        }
        //testCase-后置动作
        reportDataContext.setCurrentTitle(LOG_TITLE_POST_ACTIONS);
        postActions(context);
        log.info("□□□□[{}]executeTestCase end", tLogPrefix);
        if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("□□□□[%s]executeTestCase end", tLogPrefix));
        } else {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("□□□□executeTestCase end"));
        }
        if (reportDataContext.getStatus() == TestReportDataTestCaseDTO.STATUS_ASSERTION_ERROR) {
            throw new AssertionError("测试任务断言失败，请查看明细");
        }
    }

    private List<Map<String, Object>> getPreparationData(TestCasesRunContext context) {
        DataPreparationConfigDTO configDTO = context.getTestCasesDTO().getDataPreparationConfig();
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        if (configDTO == null) {
            log.info("√[{}] no DataPreparation need execute", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s] no DataPreparation need execute", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√no DataPreparation need execute"));
            }
            reportDataContext.logText("无配置");
            return null;
        } else {
            reportDataContext.logText(String.format("获取配置:%s", JsonUtils.INSTANCE.toPrettyJSONString(configDTO)));
            List<Map<String, Object>> dataList = dataPreparationExecutor.getPreparationData(context, configDTO);
            log.info("√[{}]getPreparationData, dataList:{}", tLogPrefix, JsonUtils.INSTANCE.toJSONString(dataList));
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s]getPreparationData, dataList:%s", tLogPrefix, JsonUtils.INSTANCE.toJSONString(dataList)));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√getPreparationData, dataList:%s", JsonUtils.INSTANCE.toJSONString(dataList)));
            }
            reportDataContext.logText(String.format("返回数据:%s", JsonUtils.INSTANCE.toPrettyJSONString(dataList)));
            return dataList;
        }
    }

    private void processPreparationData(TestCasesRunContext context, Map<String, Object> data) {
        context.addGlobalParams(data);
    }

    private void globalParamsConfig(TestCasesRunContext context) {
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        Map<String, Object> globalParams = winTestNGService.getGlobalParameters();
        log.info("[{}]execute globalParamsConfig, fetch globalParams:{}", tLogPrefix, JsonUtils.INSTANCE.toJSONString(globalParams));
        if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("[%s]execute globalParamsConfig, fetch globalParams:%s", tLogPrefix, JsonUtils.INSTANCE.toJSONString(globalParams)));
        } else {
            ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("execute globalParamsConfig, fetch globalParams:%s", JsonUtils.INSTANCE.toJSONString(globalParams)));
        }
        reportDataContext.logText(String.format("全局配置:%s", JsonUtils.INSTANCE.toPrettyJSONString(globalParams)));
        context.setCommonGlobalParams(globalParams);
        context.addGlobalParams(globalParams);
    }

    private void baseParamsConfig(TestCasesRunContext context) {
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        String config = context.getTestCasesDTO().getBaseParams();
        JSONObject baseParams = null;
        if (StringUtils.isNotBlank(config)) {
            try {
                baseParams = JSON.parseObject(config);
                paramsExecutor.renderDeep(baseParams, context);
            } catch (Exception e) {
                reportDataContext.logException(e);
                throw e;
            }
        }
        log.info("[{}]execute baseParamsConfig, fetch baseParams:{}", tLogPrefix, JsonUtils.INSTANCE.toJSONString(baseParams));
        ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("execute baseParamsConfig, fetch baseParams:%s", JsonUtils.INSTANCE.toJSONString(baseParams)));
        reportDataContext.logText(String.format("基础数据配置:%s", JsonUtils.INSTANCE.toPrettyJSONString(baseParams)));
        context.addGlobalParams(baseParams);
    }

    private void preActions(TestCasesRunContext context) throws Exception {
        List<ActionSetting> preActions = context.getTestCasesDTO().getPreActions();
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        if (CollectionUtils.isNotEmpty(preActions)) {
            log.info("√[{}]execute preActions", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s]execute preActions", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√execute preActions"));
            }
            reportDataContext.logText(JsonUtils.INSTANCE.toPrettyJSONString(preActions), LOG_SUB_TITLE_ACTIONS_FETCH_CONFIG);
            try {
                actionExecutor.execute(preActions, context);
            } catch (Throwable e) {
                throw e;
            } finally {
                reportDataContext.logText(JsonUtils.INSTANCE.toJSONString(context.getGlobalParams()), LOG_SUB_TITLE_ACTIONS_GLOBAL_DATA);
            }
        } else {
            log.info("√[{}] no preActions need execute", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s]no preActions need execute", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√no preActions need execute"));
            }
            reportDataContext.logText("无配置");
        }
    }

    private void mainActions(TestCasesRunContext context) {
        List<ActionSetting> mainActions = context.getTestCasesDTO().getMainActions();
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        if (CollectionUtils.isEmpty(mainActions)) {
            log.info("√[{}] no mainActions need execute", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s]no mainActions need execute", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√no mainActions need execute"));
            }
            reportDataContext.logText("无配置");
            return;
        }
        try {
            log.info("√[{}]execute mainActions", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s]execute mainActions", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√execute mainActions"));
            }
            reportDataContext.logText(JsonUtils.INSTANCE.toPrettyJSONString(context.getTestCasesDTO().getMainActions()), LOG_SUB_TITLE_ACTIONS_FETCH_CONFIG);
            actionExecutor.execute(mainActions, context);
            if (reportDataContext.getCurrentStepStatus() == TestReportDataTestCaseDTO.STATUS_ASSERTION_ERROR) {
                throw new AssertionError(String.format("测试用例: %s, 断言失败", reportDataContext.getCurrentTitle()));
            }
            reportDataContext.incSuccessCount();
        } catch (Throwable e) {
            //因为mainAcitons涉及多个case, 多个case间执行互不影响；当某个case执行失败，该测试任务失败数+1，异常最后抛出,
            reportDataContext.incFailCount();
        } finally {
            log.info("☆[{}]context globalParams:{}", tLogPrefix, JsonUtils.INSTANCE.toJSONString(context.getGlobalParams()));
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("☆[%s]context globalParams:%s", tLogPrefix, JsonUtils.INSTANCE.toJSONString(context.getGlobalParams())));
            }
            reportDataContext.logText(JsonUtils.INSTANCE.toJSONString(context.getGlobalParams()), LOG_SUB_TITLE_ACTIONS_GLOBAL_DATA);
        }
    }

    private void postActions(TestCasesRunContext context) throws Exception {
        List<ActionSetting> postActions = context.getTestCasesDTO().getPostActions();
        String tLogPrefix = WinTestNGLogUtils.INSTANCE.getTLogPrefix(context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        if (CollectionUtils.isNotEmpty(postActions)) {
            log.info("√[{}]execute postActions", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s]execute postActions", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√execute postActions"));
            }
            reportDataContext.logText(JsonUtils.INSTANCE.toPrettyJSONString(postActions), LOG_SUB_TITLE_ACTIONS_FETCH_CONFIG);
            try {
                actionExecutor.execute(postActions, context);
            } catch (Throwable e) {
                throw e;
            } finally {
                reportDataContext.logText(JsonUtils.INSTANCE.toJSONString(context.getGlobalParams()), LOG_SUB_TITLE_ACTIONS_GLOBAL_DATA);
            }
        } else {
            log.info("√[{}] no postActions need execute", tLogPrefix);
            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√[%s] no postActions need execute", tLogPrefix));
            } else {
                ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("√no postActions need execute"));
            }
            reportDataContext.logText("无配置");
        }
    }

    public void executeActionTemplate(Long templateId, Map<String, Object> mappings, String uuid) throws Exception {
        TestCasesRunContext context = new TestCasesRunContext(null);
        TestReportDataTestCaseDTO reportDataContext = new TestReportDataTestCaseDTO();
        reportDataContext.setReportUuid(uuid);
        reportDataContext.setStartTime(new Date());
        reportDataContext.setDebugFlag(true);
        context.setReportDataContext(reportDataContext);
        List<ActionSetting> actionSettingList = new ArrayList<>();
        TplActionSetting setting = new TplActionSetting();
        setting.setName("执行动作模板调试");
        setting.setActionType(ActionType.TPL);
        setting.setTemplateId(templateId);
        setting.setMappings(new LinkedHashMap<>(mappings));
        actionSettingList.add(setting);
        try {
            //设置全局变量
            globalParamsConfig(context);
            //执行
            actionExecutor.execute(actionSettingList, context);
        } catch (Throwable e) {
            ConsoleLogUtils.INSTANCE.logE(reportDataContext.getReportUuid(), e);
            log.error("actionExecutor.execute error", e);
        } finally {
            try {
                //清理模板并发缓存
                TestReportDataTestCaseDTO testCaseDTO = context.getReportDataContext();
                List<String> keys = new ArrayList<>();
                if (MapUtils.isNotEmpty(testCaseDTO.getTplLockCache())) {
                    keys.addAll(testCaseDTO.getTplLockCache().keySet());
                }
                cacheService.clearTplConcurrentCacheForReportUuid(testCaseDTO.getReportUuid(), keys);
                String message = String.format("清理缓存，keys: %s, reportUuid: %s", JsonUtils.INSTANCE.toJSONString(keys), testCaseDTO.getReportUuid());
                log.info(message);
                ConsoleLogUtils.INSTANCE.log(testCaseDTO.getReportUuid(), message);
            } catch (Exception e) {
                log.error("winTestNGExecutor clearTplLockCacheForActionTemplate error", e);
            }
        }
        reportDataContext.setEndTime(new Date());
    }

    public void doWorkAfterFinished(TestReportDataSummaryDTO summaryData) {
        if (summaryData.getTotalCount() > 0) {
            try {
                //清理模板并发缓存
                List<String> keys = new ArrayList<>();
                for (TestReportDataTestCaseDTO testCaseData : summaryData.getTestCaseDataList()) {
                    if (MapUtils.isNotEmpty(testCaseData.getTplLockCache())) {
                        keys.addAll(testCaseData.getTplLockCache().keySet());
                    }
                }
                cacheService.clearTplConcurrentCacheForReportUuid(summaryData.getReportUuid(), keys);
                String message = String.format("清理缓存，keys: %s, reportUuid: %s", JsonUtils.INSTANCE.toJSONString(keys), summaryData.getReportUuid());
                log.info(message);
                ConsoleLogUtils.INSTANCE.log(summaryData.getReportUuid(), message);
            } catch (Exception e) {
                log.error("winTestNGExecutor clearTplLockCacheForActionTemplate error", e);
            }
            winTestNGService.saveTestReportData(summaryData);
            //自动触发发送邮件
            String reportUrl = reportDomain + "/view/report/summary/" + summaryData.getReportUuid();
            if (Arrays.asList(TriggerMode.AUTO, TriggerMode.CRON).contains(summaryData.getTriggerMode())) {
                winTestNGService.sendReportEmail(summaryData, reportUrl);
            }
            //分析数据
            if (BooleanUtils.isTrue(summaryData.getAnalysisFlag()) && summaryData.getProjectId() != null) {
                analysisService.statisticApiFinishData(summaryData.getProjectId());
                analysisService.analysisProjectQualityScore(summaryData);
            }
            log.info("see the report at: {}", reportUrl);
        } else {
            log.info("no testTask online, has no report");
        }
    }

    /**
     * 是否需要跳过自动化测试
     *
     * @param projectId
     * @return
     */
    public boolean needSkipAutomatedTesting(Long projectId) {
        //获取項目自动化测试最后次的执行时间
        Date lastTime = winTestNGService.getLastAutomatedTestingRuntimeByProjectId(projectId);
        if (lastTime == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        // 10小时内跳过
        if (now - lastTime.getTime() < 3600 * 1000 * 10) {
            log.info("skip AutomatedTesting, projectId: {}", projectId);
            return true;
        }
        return false;
    }
}
