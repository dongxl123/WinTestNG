package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.TestReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface TestReportRepository extends JpaRepository<TestReportEntity, Long>, JpaSpecificationExecutor<TestReportEntity> {

    @Query(value = "SELECT IFNULL(sum(t.fail_count=0)/count(*),0) FROM test_report t WHERE t.trigger_mode IN (1, 4) AND t.deleted = FALSE AND t.create_time BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 DAY)  AND CURDATE() AND EXISTS ( SELECT 1 FROM test_report_details a, test_cases b WHERE a.report_uuid = t.report_uuid AND a.test_cases_id = b.id AND a.deleted = FALSE AND b.project_id = ?1 )", nativeQuery = true)
    double getSuccessRateInLastWeekForProjectQualityScore(Long projectId);

    @Query(value = "SELECT IFNULL(sum(k.exceptions IS NULL AND k.fail_count = 0)/count(*),0) FROM test_report t, test_report_details k WHERE t.trigger_mode IN (1, 4) AND t.deleted = FALSE AND t.create_time BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND CURDATE() AND t.report_uuid = k.report_uuid AND k.deleted = FALSE AND k.test_cases_id = ?1", nativeQuery = true)
    double getSuccessRateInLastWeekForTestCasesQualityScore(Long projectId);
}
