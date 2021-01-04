package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.TestCasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface TestCasesRepository extends JpaRepository<TestCasesEntity, Long>, JpaSpecificationExecutor<TestCasesEntity> {

    List<TestCasesEntity> findByIdIn(List<Long> ids);

    @Query("select a.qualityScoreOrigin from TestCasesEntity a where a.ciFlag=true and a.deleted=false and a.qualityScoreOrigin is not null")
    double[] findQualityScoreSamples();

    @Query(value = "select IFNULL(avg(a.quality_score),0) from test_cases a where a.ci_flag=true and a.deleted=false and a.quality_score is not null and a.project_id=?1", nativeQuery = true)
    double getMeanQualityScoreByProjectId(Long projectId);
}
