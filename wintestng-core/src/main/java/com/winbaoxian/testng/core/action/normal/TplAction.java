package com.winbaoxian.testng.core.action.normal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.core.action.ActionExecutor;
import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.TplActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.ActionTemplateDTO;
import com.winbaoxian.testng.service.CacheService;
import com.winbaoxian.testng.service.WinTestNGService;
import com.winbaoxian.testng.utils.ConsoleLogUtils;
import com.winbaoxian.testng.utils.CopyUtils;
import com.winbaoxian.testng.utils.WinTestNGLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 17:46
 */
@Component
@Slf4j
public class TplAction implements IAction<TplActionSetting> {

    @Resource
    private ActionExecutor actionExecutor;
    @Resource
    private ParamsExecutor paramsExecutor;
    @Resource
    private WinTestNGService winTestNGService;
    @Resource
    private CacheService cacheService;
    private static long SLEEP_TIME = 1000 * 5L;
    private static long MAX_WAIT_TIME = 1000 * 60 * 5L;

    /**
     * @param actionSetting
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Object execute(TplActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        if (actionSetting == null || actionSetting.getTemplateId() == null) {
            return null;
        }
        ActionTemplateDTO actionTemplateDTO = winTestNGService.getActionTemplate(actionSetting.getTemplateId());
        if (actionTemplateDTO == null) {
            throw new WinTestNgException(String.format("action template(%s) can not be found", actionSetting.getTemplateId()));
        }
        if (BooleanUtils.isNotFalse(actionTemplateDTO.getDeleted())) {
            throw new WinTestNgException(String.format("this action template(%s) is not available", actionSetting.getTemplateId()));
        }
        if (CollectionUtils.isEmpty(actionTemplateDTO.getActions())) {
            throw new WinTestNgException(String.format("no action set in template(%s)", actionSetting.getTemplateId()));
        }
        String[] testReportTitles = WinTestNGLogUtils.INSTANCE.getTestReportTitles(actionSetting, context);
        String tFlowActionLogPrefix = WinTestNGLogUtils.INSTANCE.getActionLogPrefix(actionSetting, context);
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        String config = actionTemplateDTO.getBaseParams();
        JSONObject baseParams = null;
        if (StringUtils.isNotBlank(config)) {
            try {
                baseParams = JSON.parseObject(config);
                paramsExecutor.renderDeep(baseParams, context);
            } catch (Exception e) {
                reportDataContext.logException(e, testReportTitles);
                throw e;
            }
        }
        context.addGlobalParams(baseParams);
        log.info("[{}]execute template, fetch baseParams:{}", tFlowActionLogPrefix, JsonUtils.INSTANCE.toJSONString(baseParams));
        ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("execute tpl(%s), fetch baseParams:%s", actionSetting.getTemplateId(), JsonUtils.INSTANCE.toJSONString(baseParams)));
        reportDataContext.logText(String.format("模板基础数据配置:%s", JsonUtils.INSTANCE.toJSONString(baseParams)), testReportTitles);
        boolean useCacheFlag = false;
        if (BooleanUtils.isTrue(actionTemplateDTO.getConcurrentCacheSupport()) && MapUtils.isNotEmpty(actionSetting.getMappings())) {
            useCacheFlag = true;
        }
        if (useCacheFlag) {
            StringBuilder keySb = new StringBuilder();
            keySb.append(String.format("TPL_%s", actionSetting.getTemplateId()));
            List<String> paramsKeys = new ArrayList<>(actionSetting.getMappings().keySet());
            Collections.sort(paramsKeys);
            for (String paramsKey : paramsKeys) {
                keySb.append(String.format("-%s_%s", paramsKey, actionSetting.getMappings().get(paramsKey)));
            }
            String key = keySb.toString();
            //多个线程，参数一样，调用同一个模板，加锁lockKey，按顺序执行
            String lockKey = key + ".lock";
            long startTime = System.currentTimeMillis();
            String reportUuid = reportDataContext.getReportUuid();
            while (true) {
                boolean fetchLock = cacheService.tryFetchLock(lockKey);
                if (fetchLock) {
                    try {
                        //查询缓存
                        Object cacheObj = cacheService.getTplConcurrentTplCache(key);
                        //命中
                        if (cacheObj != null) {
                            //设置缓存
                            if (!cacheService.hasTplConcurrentTplCache(key, reportUuid)) {
                                cacheService.setTplConcurrentTplCache(key, reportUuid, cacheObj);
                                context.getReportDataContext().getTplLockCache().put(key, cacheObj);
                            }
                            //日志
                            String message = String.format("[%s] TPL(%s): %s, 命中缓存, key: %s, value: %s", tFlowActionLogPrefix, actionTemplateDTO.getId(), actionTemplateDTO.getName(), key, JsonUtils.INSTANCE.toJSONString(cacheObj));
                            log.info(message);
                            if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                                ConsoleLogUtils.INSTANCE.log(reportUuid, message);
                            } else {
                                ConsoleLogUtils.INSTANCE.log(reportUuid, String.format("%s, 命中缓存, key: %s", actionTemplateDTO.getName(), key));
                            }
                            reportDataContext.logText(String.format("%s, 命中缓存, key: %s", actionTemplateDTO.getName(), key), testReportTitles);
                            return cacheObj;
                        }
                        Object retObject = executeInternal(actionSetting, actionTemplateDTO, context);
                        cacheService.setTplConcurrentTplCache(key, reportUuid, retObject);
                        reportDataContext.getTplLockCache().put(key, retObject);
                        String message = String.format("[%s] TPL(%s): %s, 添加缓存, key: %s, value: %s", tFlowActionLogPrefix, actionTemplateDTO.getId(), actionTemplateDTO.getName(), key, JsonUtils.INSTANCE.toJSONString(retObject));
                        log.info(message);
                        if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                            ConsoleLogUtils.INSTANCE.log(reportUuid, message);
                        } else {
                            ConsoleLogUtils.INSTANCE.log(reportUuid, String.format("%s, 添加缓存, key: %s", actionTemplateDTO.getName(), key));
                        }
                        reportDataContext.logText(String.format("%s, 添加缓存, key: %s", actionTemplateDTO.getName(), key), testReportTitles);
                        return retObject;
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        cacheService.removeLock(lockKey);
                    }
                } else if (System.currentTimeMillis() - startTime <= MAX_WAIT_TIME) {
                    Thread.sleep(SLEEP_TIME);
                } else {
                    throw new WinTestNgException(String.format("[%s] TPL(%s): %s, 等待锁失败, key: %s", tFlowActionLogPrefix, actionTemplateDTO.getId(), actionTemplateDTO.getName(), key));
                }
            }
        } else {
            return executeInternal(actionSetting, actionTemplateDTO, context);
        }
    }

    private Object executeInternal(TplActionSetting actionSetting, ActionTemplateDTO actionTemplateDTO, TestCasesRunContext context) throws Exception {
        context.pushActionSetting(actionSetting);
        try { //shallow copy TestCasesRunContext
            TestCasesRunContext newContext = CopyUtils.INSTANCE.shallowClone(context);
            newContext.setGlobalParams(new HashMap<>());
            //测试模板支持全局变量
            Map<String, Object> commonGlobalParams = context.getCommonGlobalParams();
            if (MapUtils.isNotEmpty(commonGlobalParams)) {
                newContext.addGlobalParams(commonGlobalParams);
            }
            newContext.addGlobalParams(actionSetting.getMappings());
            actionExecutor.execute(actionTemplateDTO.getActions(), newContext);
            if (StringUtils.isEmpty(actionTemplateDTO.getResult())) {
                return null;
            }
            String result = paramsExecutor.render(actionTemplateDTO.getResult(), newContext);
            return JsonUtils.INSTANCE.parseObject(result);
        } catch (Exception e) {
            throw e;
        } finally {
            context.popActionSetting();
        }
    }

}
