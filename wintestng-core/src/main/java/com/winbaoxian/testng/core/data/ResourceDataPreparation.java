package com.winbaoxian.testng.core.data;

import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.core.resource.ResourceExecutor;
import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.ResourceActionSetting;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:00
 */
@Component
public class ResourceDataPreparation implements IDataPreparation {

    @Resource
    private ResourceExecutor resourceExecutor;
    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public List<Map<String, Object>> getPreparationData(DataPreparationConfigDTO config, TestCasesRunContext context) {
        Long resourceId = config.getResourceId();
        String sql = config.getSql();
        ResourceActionSetting settings = new ResourceActionSetting();
        settings.setResourceId(resourceId);
        settings.setSql(paramsExecutor.render(sql, context));
        return (List<Map<String, Object>>) resourceExecutor.executeDbQuery(settings);
    }
}
