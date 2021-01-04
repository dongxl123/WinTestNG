package com.winbaoxian.testng.core;

import com.winbaoxian.testng.BaseTest;
import com.winbaoxian.testng.utils.UUIDUtil;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-22 18:45
 */
public class WinTestNGExecutorTest extends BaseTest {

    @Resource
    private WinTestNGExecutor winTestNGExecutor;

    @Test
    public void testExecuteActionTemplate() throws Exception {
        Long templateId = 66L;
        Map<String, Object> params = new HashMap<>();
        String uuid = UUIDUtil.INSTANCE.randomUUID();
        winTestNGExecutor.executeActionTemplate(templateId, params, uuid);
    }
}
