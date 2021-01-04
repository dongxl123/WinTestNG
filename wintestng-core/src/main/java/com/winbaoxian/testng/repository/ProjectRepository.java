package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, JpaSpecificationExecutor<ProjectEntity> {

    @Query(value = "select ifnull(a.quality_score,0) from project a where a.id=?1", nativeQuery = true)
    double getQualityScoreByProjectId(Long projectId);
}
