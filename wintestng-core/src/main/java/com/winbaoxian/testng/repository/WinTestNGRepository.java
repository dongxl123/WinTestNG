package com.winbaoxian.testng.repository;

import com.winbaoxian.testng.enums.TriggerMode;
import com.winbaoxian.testng.model.entity.TestCasesEntity;
import org.hibernate.SQLQuery;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 17:10
 */
@Repository
public class WinTestNGRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TestCasesEntity> getTestCasesList(String sql) {
        Query query = entityManager.createNativeQuery(sql, TestCasesEntity.class);
        return query.getResultList();
    }

    public Date getLastAutomatedTestingRuntimeByProjectId(Long projectId) {
        String sql = "SELECT a.start_time startTime " +
                "FROM test_report a, test_report_details b, test_cases c " +
                "WHERE a.report_uuid = b.report_uuid AND b.test_cases_id = c.id " +
                "AND a.deleted = FALSE AND b.deleted = FALSE AND c.deleted = FALSE " +
                "AND a.trigger_mode in (:triggerModeList) AND c.project_id=:projectId " +
                "ORDER BY a.id DESC";
        org.hibernate.Query query = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class)
                .addScalar("startTime", TimestampType.INSTANCE)
                .setParameterList("triggerModeList", Arrays.asList(TriggerMode.AUTO.getValue(), TriggerMode.CRON.getValue()))
                .setParameter("projectId", projectId)
                .setMaxResults(1);
        return (Date) query.uniqueResult();
    }

}
