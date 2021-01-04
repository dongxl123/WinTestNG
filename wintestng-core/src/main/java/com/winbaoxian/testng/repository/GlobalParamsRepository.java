package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.GlobalParamsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface GlobalParamsRepository extends JpaRepository<GlobalParamsEntity, Long>, JpaSpecificationExecutor<GlobalParamsEntity> {

    List<GlobalParamsEntity> findByDeletedFalse();

}
