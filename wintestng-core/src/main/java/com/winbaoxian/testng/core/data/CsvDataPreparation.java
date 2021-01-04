package com.winbaoxian.testng.core.data;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:00
 */
@Component
@Slf4j
public class CsvDataPreparation implements IDataPreparation {

    @Value("${project.filePath:files}")
    private String filePath;
    @Resource
    private ParamsExecutor paramsExecutor;

    @Override
    public List<Map<String, Object>> getPreparationData(DataPreparationConfigDTO config, TestCasesRunContext context) {
        Character separator = (config.getSeparator() == null) ? WinTestNGConstant.CHAR_COMMA.charAt(0) : config.getSeparator();
        List<String> fieldNameList = config.getFieldNameList();
        String fileName = config.getFileName();
        if (CollectionUtils.isEmpty(fieldNameList) || StringUtils.isBlank(fileName)) {
            return null;
        }
        BufferedReader br = null;
        List<Map<String, Object>> retList = new ArrayList<>();
        try {
            if (StringUtils.startsWithIgnoreCase(fileName, WinTestNGConstant.STRING_HTTP)) {
                byte[] bytes = HttpUtils.INSTANCE.doFileGetWithRetry(fileName);
                br = new BufferedReader(new StringReader(new String(bytes, "utf-8")));
            } else {
                File csvFile = new File(filePath + File.separator + fileName);
                br = new BufferedReader(new FileReader(csvFile));
            }
            String line;
            // 读取到的内容给line变量
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                line = paramsExecutor.render(line, context);
                Map<String, Object> map = new HashMap<>();
                String[] fieldValues = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, separator.toString());
                int length = Math.min(fieldNameList.size(), fieldValues.length);
                for (int i = 0; i < length; i++) {
                    map.put(fieldNameList.get(i), StringUtils.strip(fieldValues[i], WinTestNGConstant.CHAR_DOUBLE_QUOTA));
                }
                retList.add(map);
            }
        } catch (Exception e) {
            log.error("读取csv文件出错，fileName:{}", fileName, e);
            throw new WinTestNgException(String.format("读取csv文件出错，fileName:%s", fileName), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("流关闭异常", e);
                }
            }
        }
        return retList;
    }
}
