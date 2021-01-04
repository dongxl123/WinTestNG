package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.model.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * (Api)Repository类
 *
 * @author dongxuanliang252
 * @date 2020-05-25 15:08:03
 */
public interface ApiRepository extends JpaRepository<ApiEntity, Long>, JpaSpecificationExecutor<ApiEntity> {

    ApiEntity findTopByProjectIdAndApiUrlAndDeletedFalse(Long projectId, String apiUrl);

    ApiEntity findTopByProjectIdAndApiUrlLikeAndDeletedFalse(Long projectId, String apiUrl);

    long countByProjectIdAndTargetFlagTrueAndDeletedFalse(Long projectId);

    long countByProjectIdAndTargetFlagTrueAndFinishFlagTrueAndDeletedFalse(Long projectId);

}