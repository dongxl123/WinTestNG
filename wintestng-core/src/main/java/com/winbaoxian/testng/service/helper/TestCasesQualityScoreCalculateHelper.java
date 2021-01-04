package com.winbaoxian.testng.service.helper;

import com.winbaoxian.testng.enums.ActionType;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.AbstractActionSetting;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import com.winbaoxian.testng.model.core.action.LogicActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.NormalDistributionDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.repository.TestCasesRepository;
import com.winbaoxian.testng.repository.TestReportRepository;
import com.winbaoxian.testng.service.CacheService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Log4j
public class TestCasesQualityScoreCalculateHelper {


    private final static String CACHE_KEY_TESTS_CASES_QUALITY_SCORE_NORMAL_DATA = "tests_cases_quality_score_normal_data";
    /**
     * 衰减系数
     */
    private final static double ATTENUATION_COEFFICIENT = 0.95;


    @Resource
    private CacheService cacheService;
    @Resource
    private TestCasesRepository testCasesRepository;
    @Resource
    private TestReportRepository testReportRepository;

    /**
     * 1. 计算基础分
     * 2. 计算加分项
     * 3. 计算减分项
     * 4. 计算加减分系数, 先按1计算
     * 5. 计算质量分（原始值）= max(基础分+(加分项-减分项)*加减分系数, 上次测试任务质量分*衰减系数)
     * 6. 计算质量分（最终值）按正态分布后计算得到，测试任务质量分
     *
     * @param context
     * @return
     */
    public double calculateQualityScoreOrigin(TestCasesRunContext context) {
        //1. 计算基础分
        double base = calculateBaseScore(context);
        //2. 计算加分项
        double add = calculateAddScore(context);
        //3. 计算减分项
        double minus = calculateMinusScore(context);
        //4. 计算加减分系数
        double coefficient = calculateScoreCoefficient(context.getTestCasesDTO().getProjectId());
        //上次测试任务质量分
        double last = context.getTestCasesDTO().getQualityScoreOrigin() == null ? 0 : context.getTestCasesDTO().getQualityScoreOrigin();
        //5. 计算质量分（原始值）= max(基础分+(加分项-减分项)*加减分系数, 上次测试任务质量分*衰减系数)
        double origin = Math.max(base + (add - minus) * coefficient, last * ATTENUATION_COEFFICIENT);
        //6. 计算质量分（最终值）按正态分布后计算得到，测试任务质量分
        return origin;
    }

    /**
     * 基础分计算（最多4分）
     * 步骤类型（主流程）含有接口、断言， 每一种类型+1，其他类型不计
     * 执行时间 <=5分钟 +1
     * 最近一周测试任务成功率(去掉断言失败) +1*成功率
     *
     * @param context
     * @return
     */
    private double calculateBaseScore(TestCasesRunContext context) {
        TestCasesDTO testCasesDTO = context.getTestCasesDTO();
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        double score = 0;
        List<ActionSetting> mainActions = testCasesDTO.getMainActions();
        if (CollectionUtils.isNotEmpty(mainActions)) {
            Set<ActionType> actionTypeSet = getActionTypeSet(mainActions);
            if (actionTypeSet.contains(ActionType.HTTP)) {
                score += 1;
            }
            if (actionTypeSet.contains(ActionType.ASSERTION)) {
                score += 1;
            }
        }
        if (reportDataContext.getDuration() <= 1000 * 60 * 5) {
            score += 1;
        }
        double lastWeekSuccessRate = testReportRepository.getSuccessRateInLastWeekForTestCasesQualityScore(testCasesDTO.getId());
        score += lastWeekSuccessRate;
        return score;
    }

    /**
     * 加分项计算
     * 步骤数量count(前置、主流程、后置)
     * *    前置 1<count<=10 +0.5
     * *    主流程 10<=count<30 +0.5
     * *    后置 1<count<=10 +0.5
     * *    主流程count>=前置count+后置count  +0.5
     * 使用基础数据 +0.5
     * 用例数量 2<=count<=20 +1
     * 步骤使用template +0.5
     * 使用group、if或for +0.5
     * 断言数量占比>=1/4 +1
     * 断言失败  +0.5
     *
     * @param context
     * @return
     */
    private double calculateAddScore(TestCasesRunContext context) {
        TestCasesDTO testCasesDTO = context.getTestCasesDTO();
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        double score = 0;
        List<ActionSetting> preActions = null;
        List<ActionSetting> mainActions = null;
        List<ActionSetting> postActions = null;
        List<ActionSetting> allActions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(testCasesDTO.getPreActions())) {
            allActions.addAll(preActions);
        }
        if (CollectionUtils.isNotEmpty(testCasesDTO.getMainActions())) {
            allActions.addAll(mainActions);
        }
        if (CollectionUtils.isNotEmpty(testCasesDTO.getPostActions())) {
            allActions.addAll(postActions);
        }
        int preCount = getActionSettingsNormalCount(preActions);
        int mainCount = getActionSettingsNormalCount(mainActions);
        int postCount = getActionSettingsNormalCount(postActions);
        if (preCount > 1 && preCount <= 10) {
            score += 0.5;
        }
        if (mainCount >= 10 && mainCount <= 30) {
            score += 0.5;
        }
        if (postCount > 1 && postCount <= 10) {
            score += 0.5;
        }
        if (mainCount >= preCount + postCount) {
            score += 0.5;
        }
        if (StringUtils.isNotBlank(testCasesDTO.getBaseParams())) {
            score += 0.5;
        }
        long testCasesCount = reportDataContext.getTotalCount();
        if (testCasesCount >= 1 && testCasesCount <= 20) {
            score += 1;
        }
        if (CollectionUtils.isNotEmpty(allActions)) {
            Set<ActionType> actionTypeSet = getActionTypeSet(allActions);
            if (actionTypeSet.contains(ActionType.TPL)) {
                score += 0.5;
            }
            if (CollectionUtils.containsAny(actionTypeSet, Arrays.asList(ActionType.GROUP, ActionType.IF, ActionType.FOR))) {
                score += 0.5;
            }
            int assertCount = getActionSettingsCountByActionType(allActions, ActionType.ASSERTION);
            int totalCount = getActionSettingsNormalCount(allActions);
            if (assertCount * 1.0 / totalCount >= 1.0 / 4) {
                score += 1;
            }
        }
        if (reportDataContext.getStatus() == TestReportDataTestCaseDTO.STATUS_ASSERTION_ERROR) {
            score += 1;
        }
        return score;
    }

    /**
     * 减分项计算
     * 步骤失败  -2
     * 用例数量>20 -1
     *
     * @param context
     * @return
     */
    private double calculateMinusScore(TestCasesRunContext context) {
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        double score = 0;
        if (reportDataContext.getStatus() == TestReportDataTestCaseDTO.STATUS_EXCEPTION) {
            score += 2;
        }
        if (reportDataContext.getTotalCount() > 20) {
            score += 1;
        }
        return score;
    }

    private double calculateScoreCoefficient(Long projectId) {
        return 1d;
    }

    /**
     * 计算质量分（最终值）按正态分布后计算得到，测试任务质量分
     * 0-3     5%
     * 3-5     10%
     * 5-7     35%
     * 7-9     40%
     * 9-10    10%
     *
     * @param origin
     * @return
     */
    private final static double[] FIXED_SAMPLES = {0, 1, 2, 3, 5, 7, 9, 10};

    public double transformToTargetScoreWithNormal(double origin) {
        NormalDistributionDTO dto = cacheService.getQualityScoreNormalDistributionDTO(CACHE_KEY_TESTS_CASES_QUALITY_SCORE_NORMAL_DATA);
        if (dto == null) {
            dto = new NormalDistributionDTO();
            double[] qualitySamples = testCasesRepository.findQualityScoreSamples();
            if (ArrayUtils.isNotEmpty(qualitySamples) && qualitySamples.length >= 5) {
                //增加固定样本
                qualitySamples = ArrayUtils.addAll(qualitySamples, FIXED_SAMPLES);
                StandardDeviation standardDeviation = new StandardDeviation();
                dto.setCount(qualitySamples.length);
                dto.setMean(StatUtils.mean(qualitySamples));
                dto.setStd(standardDeviation.evaluate(qualitySamples));
                wrapLinearTransformFunctionList(dto);
                if (qualitySamples.length - FIXED_SAMPLES.length >= 10) {
                    cacheService.setQualityScoreNormalDistributionDTO(CACHE_KEY_TESTS_CASES_QUALITY_SCORE_NORMAL_DATA, dto);
                }
            } else {
                dto.setCount(0L);
                dto.setMean(5);
                dto.setStd(Math.sqrt(1));
                wrapLinearTransformFunctionList(dto);
            }
        }
        List<NormalDistributionDTO.LinearTransformFunction> linearTransformFunctionList = dto.getLinearTransformFunctionList();
        if (CollectionUtils.isNotEmpty(linearTransformFunctionList)) {
            for (NormalDistributionDTO.LinearTransformFunction function : linearTransformFunctionList) {
                if (origin >= function.getNormalStart() && origin < function.getNormalEnd()) {
                    return function.getTargetValue(origin);
                }
            }
            if (origin <= linearTransformFunctionList.get(0).getNormalStart()) {
                return linearTransformFunctionList.get(0).getTargetStart();
            }
            if (origin >= linearTransformFunctionList.get(linearTransformFunctionList.size() - 1).getNormalStart()) {
                return linearTransformFunctionList.get(linearTransformFunctionList.size() - 1).getTargetStart();
            }
        }
        return Math.max(0, Math.min(10, origin));
    }

    private final static double[][] QUALITY_SCORE_SETTINGS = {{1, 3, 0.05}, {3, 5, 0.1}, {5, 7, 0.35}, {7, 9, 0.4}, {9, 10, 0.1}};

    private void wrapLinearTransformFunctionList(NormalDistributionDTO dto) {
        if (dto == null) {
            return;
        }
        List<NormalDistributionDTO.LinearTransformFunction> functionList = new ArrayList<>();
        dto.setLinearTransformFunctionList(functionList);
        NormalDistribution normal = new NormalDistribution(dto.getMean(), dto.getStd());
        double cdf = 0;
        for (double[] settings : QUALITY_SCORE_SETTINGS) {
            double targetStart = settings[0];
            double targetEnd = settings[1];
            double preCdf = cdf;
            cdf += settings[2];
            double normalStart = normal.inverseCumulativeProbability(getValidProbability(preCdf));
            double normalEnd = normal.inverseCumulativeProbability(getValidProbability(cdf));
            NormalDistributionDTO.LinearTransformFunction function = new NormalDistributionDTO.LinearTransformFunction();
            function.setTargetStart(targetStart);
            function.setTargetEnd(targetEnd);
            function.setNormalStart(normalStart);
            function.setNormalEnd(normalEnd);
            functionList.add(function);
        }
    }

    private double getValidProbability(double cdf) {
        double deltaValue = Math.pow(10, -5);
        if (cdf <= 0) {
            return deltaValue;
        }
        if (cdf >= 1) {
            return 1 - deltaValue;
        }
        return cdf;
    }

    private Set<ActionType> getActionTypeSet(List<ActionSetting> actionSettings) {
        if (CollectionUtils.isEmpty(actionSettings)) {
            return Collections.EMPTY_SET;
        }
        Set<ActionType> actionTypeSet = new HashSet<>();
        for (ActionSetting o : actionSettings) {
            if (!(o instanceof AbstractActionSetting)) {
                continue;
            }
            AbstractActionSetting setting = (AbstractActionSetting) o;
            if (BooleanUtils.isNotTrue(setting.getDisableFlag())) {
                continue;
            }
            actionTypeSet.add(setting.getActionType());
            if (o instanceof LogicActionSetting) {
                actionTypeSet.addAll(getActionTypeSet(((LogicActionSetting) o).getStepList()));
            }
        }
        return actionTypeSet;
    }

    /**
     * 去掉for、if、group
     */
    private int getActionSettingsNormalCount(List<ActionSetting> actionSettings) {
        if (CollectionUtils.isEmpty(actionSettings)) {
            return 0;
        }
        int count = 0;
        for (ActionSetting o : actionSettings) {
            if (o instanceof AbstractActionSetting) {
                continue;
            }
            AbstractActionSetting setting = (AbstractActionSetting) o;
            if (BooleanUtils.isNotTrue(setting.getDisableFlag())) {
                continue;
            }
            if (o instanceof LogicActionSetting) {
                count += getActionSettingsNormalCount(((LogicActionSetting) o).getStepList());
            } else {
                count = +1;
            }
        }
        return count;
    }


    private int getActionSettingsCountByActionType(List<ActionSetting> actionSettings, ActionType actionType) {
        if (CollectionUtils.isEmpty(actionSettings)) {
            return 0;
        }
        int count = 0;
        for (ActionSetting o : actionSettings) {
            if (o instanceof AbstractActionSetting) {
                continue;
            }
            AbstractActionSetting setting = (AbstractActionSetting) o;
            if (BooleanUtils.isNotTrue(setting.getDisableFlag())) {
                continue;
            }
            if (actionType.equals(setting.getActionType())) {
                count = +1;
            }
            if (o instanceof LogicActionSetting) {
                count += getActionSettingsCountByActionType(((LogicActionSetting) o).getStepList(), actionType);
            }
        }
        return count;
    }


}
