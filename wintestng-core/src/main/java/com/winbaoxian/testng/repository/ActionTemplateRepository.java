package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.ActionTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface ActionTemplateRepository extends JpaRepository<ActionTemplateEntity, Long>, JpaSpecificationExecutor<ActionTemplateEntity> {

}
