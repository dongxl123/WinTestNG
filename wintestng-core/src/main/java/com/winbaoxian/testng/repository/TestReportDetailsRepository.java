package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.TestReportDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface TestReportDetailsRepository extends JpaRepository<TestReportDetailsEntity, Long>, JpaSpecificationExecutor<TestReportDetailsEntity> {

}
