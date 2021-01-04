package com.winbaoxian.testng.core.data;

import com.winbaoxian.testng.enums.DataPreparationType;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 14:14
 */
@Component
@Slf4j
public class DataPreparationExecutor {

    public List<Map<String, Object>> getPreparationData(TestCasesRunContext context, DataPreparationConfigDTO config) {
        DataPreparationType type = config.getType();
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        try {
            IDataPreparation iDataPreparation = determineDataPreparation(type);
            if (iDataPreparation == null) {
                log.info("没有找到数据准备执行器,type:{}", type);
                throw new WinTestNgException(String.format("没有找到数据准备执行器, type:%s", type));
            }
            return iDataPreparation.getPreparationData(config, context);
        } catch (Exception e) {
            reportDataContext.logException(e);
            throw e;
        }
    }

    @Resource
    private ApplicationContext applicationContext;

    private IDataPreparation determineDataPreparation(DataPreparationType type) {
        if (type == null) {
            return null;
        }
        String beanName = String.format("%sDataPreparation", type);
        return applicationContext.getBean(beanName, IDataPreparation.class);
    }

}
