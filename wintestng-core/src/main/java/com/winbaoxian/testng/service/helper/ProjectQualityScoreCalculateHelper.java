package com.winbaoxian.testng.service.helper;

import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.repository.ProjectRepository;
import com.winbaoxian.testng.repository.TestCasesRepository;
import com.winbaoxian.testng.repository.TestReportRepository;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Log4j
public class ProjectQualityScoreCalculateHelper {

    /**
     * 衰减系数
     */
    private final static double ATTENUATION_COEFFICIENT = 0.95;

    @Resource
    private ProjectRepository projectRepository;
    @Resource
    private TestCasesRepository testCasesRepository;
    @Resource
    private TestReportRepository testReportRepository;

    /**
     * 1. 计算基础分 = max(上线测试用例质量分平均值-3, 0)
     * 2. 计算附加分 = min(max(附加分, 0),3)
     * 3. 计算质量分 = max(基础分+附加分, 上次质量分*衰减系数)
     *
     * @param summaryData
     * @return
     */
    public double calculateQualityScore(TestReportDataSummaryDTO summaryData) {
        Long projectId = summaryData.getProjectId();
        //1. 计算基础分
        double base = calculateBaseScore(projectId);
        //2. 计算附加分
        double extra = calculateExtraScore(summaryData);
        //3. 计算质量分 = max(基础分+附加分, 上次质量分*衰减系数)
        //上次项目质量分
        double last = projectRepository.getQualityScoreByProjectId(projectId);
        return Math.max(base + extra, last * ATTENUATION_COEFFICIENT);
    }

    /**
     * 1. 计算基础分
     * max(上线测试用例质量分平均值-3, 0)
     *
     * @param projectId
     * @return
     */
    private double calculateBaseScore(Long projectId) {
        double qualityScore = testCasesRepository.getMeanQualityScoreByProjectId(projectId);
        return Math.max(qualityScore - 3, 0);
    }

    /**
     * 2. 计算附加分
     * min(max(附加分, 0),3)
     * *本次测试任务成功率(排除断言失败) +1*成功率
     * *接口覆盖率     +1*完成率
     * *最近一周项目成功率  +1*成功率
     *
     * @param summaryData
     * @return
     */
    private double calculateExtraScore(TestReportDataSummaryDTO summaryData) {
        List<TestReportDataTestCaseDTO> testCaseDataList = summaryData.getTestCaseDataList();
        double score = 0;
        //本次测试任务成功率
        if (CollectionUtils.isNotEmpty(testCaseDataList)) {
            long totalCount = testCaseDataList.size();
            long failCount = testCaseDataList.stream().filter(o -> TestReportDataTestCaseDTO.STATUS_EXCEPTION == o.getStatus()).count();
            score += (totalCount - failCount) * 1.0 / totalCount;
        }
        //接口覆盖率
        Long projectId = summaryData.getProjectId();
        ProjectEntity project = projectRepository.findOne(projectId);
        if (project != null && project.getTargetCount() > 0) {
            score += project.getFinishCount() * 1.0 / project.getTargetCount();
        }
        //最近一周项目成功率
        double lastWeekSuccessRate = testReportRepository.getSuccessRateInLastWeekForProjectQualityScore(projectId);
        score += lastWeekSuccessRate;
        return Math.min(Math.max(score, 0), 3);
    }


}
