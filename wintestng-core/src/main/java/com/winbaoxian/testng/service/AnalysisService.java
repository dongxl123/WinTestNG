package com.winbaoxian.testng.service;

import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.entity.ApiEntity;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.model.entity.TestCasesEntity;
import com.winbaoxian.testng.repository.ApiRepository;
import com.winbaoxian.testng.repository.ProjectRepository;
import com.winbaoxian.testng.repository.TestCasesRepository;
import com.winbaoxian.testng.service.helper.ProjectQualityScoreCalculateHelper;
import com.winbaoxian.testng.service.helper.TestCasesQualityScoreCalculateHelper;
import com.winbaoxian.testng.utils.StringExtUtils;
import com.winbaoxian.testng.utils.UrlParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AnalysisService {

    @Resource
    private ApiRepository apiRepository;
    @Resource
    private ProjectRepository projectRepository;
    @Resource
    private TestCasesRepository testCasesRepository;
    @Resource
    private AnalysisService self;
    @Resource
    private TestCasesQualityScoreCalculateHelper testCasesQualityScoreCalculateHelper;
    @Resource
    private ProjectQualityScoreCalculateHelper projectQualityScoreCalculateHelper;

    private ExecutorService analysisTestCasesPool;
    private ExecutorService analysisProjectPool;

    @PostConstruct
    public void init() {
        analysisTestCasesPool = Executors.newFixedThreadPool(10);
        analysisProjectPool = Executors.newFixedThreadPool(5);
    }

    /**
     * 分析单个接口的完成状态
     *
     * @param projectId
     * @param requestUrl
     */
    @Transactional
    public void analysisApiFinishFlag(Long projectId, String requestUrl) {
        if (StringUtils.isBlank(requestUrl) || projectId == null) {
            return;
        }
        String requestPath = UrlParserUtils.INSTANCE.parseStrictRequestPath(requestUrl);
        ApiEntity entity = apiRepository.findTopByProjectIdAndApiUrlAndDeletedFalse(projectId, requestPath);
        //替换最后个数字
        if (entity == null) {
            String requestPathLikeStr = StringExtUtils.INSTANCE.replaceLast(requestPath, "/(\\d+)([/]?)", "/{%}$2");
            if (!StringUtils.equals(requestPath, requestPathLikeStr)) {
                entity = apiRepository.findTopByProjectIdAndApiUrlLikeAndDeletedFalse(projectId, requestPathLikeStr);
            }
        }
        //替换所有数字查找
        if (entity == null) {
            String requestPathLikeStr = StringUtils.replacePattern(requestPath, "/(\\d+)([/]?)", "/{%}$2");
            if (!StringUtils.equals(requestPath, requestPathLikeStr)) {
                entity = apiRepository.findTopByProjectIdAndApiUrlLikeAndDeletedFalse(projectId, requestPathLikeStr);
            }
        }
        if (entity == null || BooleanUtils.isTrue(entity.getFinishFlag())) {
            return;
        }
        entity.setFinishFlag(Boolean.TRUE);
        apiRepository.save(entity);
    }

    /**
     * 统计接口完成数据
     *
     * @param projectId
     */
    @Transactional
    public void statisticApiFinishData(Long projectId) {
        if (projectId == null) {
            return;
        }
        long targetCount = apiRepository.countByProjectIdAndTargetFlagTrueAndDeletedFalse(projectId);
        long finishCount = apiRepository.countByProjectIdAndTargetFlagTrueAndFinishFlagTrueAndDeletedFalse(projectId);
        ProjectEntity entity = projectRepository.getOne(projectId);
        if (entity == null) {
            return;
        }
        entity.setTargetCount(targetCount);
        entity.setFinishCount(finishCount);
        projectRepository.save(entity);
    }

    /**
     * 异步分析单个测试任务的质量分
     *
     * @param context
     */
    public void analysisTestCasesQualityScoreAsync(TestCasesRunContext context) {
        analysisTestCasesPool.execute(() -> self.analysisTestCasesQualityScore(context));
    }

    /**
     * 分析单个测试任务的质量分
     *
     * @param context
     */
    @Transactional
    public void analysisTestCasesQualityScore(TestCasesRunContext context) {
        try {
            double qualityScoreOrigin = testCasesQualityScoreCalculateHelper.calculateQualityScoreOrigin(context);
            double qualityScore = testCasesQualityScoreCalculateHelper.transformToTargetScoreWithNormal(qualityScoreOrigin);
            TestCasesEntity entity = testCasesRepository.findOne(context.getTestCasesDTO().getId());
            entity.setQualityScoreOrigin(qualityScoreOrigin);
            entity.setQualityScore(qualityScore);
            testCasesRepository.save(entity);
        } catch (Exception e) {
            log.error("analysisTestCasesQualityScore failed", e);
        }
    }


    /**
     * 异步分析单个项目的质量分
     *
     * @param summaryData
     */
    public void analysisProjectQualityScoreAsync(TestReportDataSummaryDTO summaryData) {
        analysisProjectPool.execute(() -> self.analysisProjectQualityScore(summaryData));
    }

    /**
     * 分析单个项目的质量分
     *
     * @param summaryData
     */
    @Transactional
    public void analysisProjectQualityScore(TestReportDataSummaryDTO summaryData) {
        try {
            double qualityScore = projectQualityScoreCalculateHelper.calculateQualityScore(summaryData);
            ProjectEntity entity = projectRepository.findOne(summaryData.getProjectId());
            entity.setQualityScore(qualityScore);
            projectRepository.save(entity);
        } catch (Exception e) {
            log.error("analysisProjectQualityScore failed", e);
        }
    }

}
