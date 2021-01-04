package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.ProjectQualityReportHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 项目质量报告(ProjectQualityReportHistory)Repository类
 *
 * @author dongxuanliang252
 * @date 2020-06-24 16:52:19
 */
public interface ProjectQualityReportHistoryRepository extends JpaRepository<ProjectQualityReportHistoryEntity, Long>, JpaSpecificationExecutor<ProjectQualityReportHistoryEntity>{


}