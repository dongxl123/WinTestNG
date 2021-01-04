package com.winbaoxian.testng.core.data;

import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.TestCasesRunContext;

import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:00
 */
public interface IDataPreparation {

    List<Map<String, Object>> getPreparationData(DataPreparationConfigDTO config, TestCasesRunContext context);
}
