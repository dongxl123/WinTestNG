package com.winbaoxian.testng.core.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:00
 */
@Component
public class TextDataPreparation implements IDataPreparation {

    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public List<Map<String, Object>> getPreparationData(DataPreparationConfigDTO config, TestCasesRunContext context) {
        String text = paramsExecutor.render(config.getText(), context);
        Object jsonObject = JsonUtils.INSTANCE.parseObject(text);
        List<Map<String, Object>> list = new ArrayList<>();
        if (jsonObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) jsonObject;
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    JSONObject m = (JSONObject) o;
                    list.add(m.getInnerMap());
                }
            }
        } else if (jsonObject instanceof JSONObject) {
            JSONObject m = (JSONObject) jsonObject;
            list.add(m.getInnerMap());
        }
        return list;
    }
}
