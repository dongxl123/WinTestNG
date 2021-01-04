package com.winbaoxian.testng.model.core;

import com.winbaoxian.testng.model.core.action.ActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 测试任务执行上下文数据
 *
 * @author dongxuanliang252
 * @date 2019-02-28 16:13
 */
@Getter
@Setter
public class TestCasesRunContext implements Serializable {

    private TestCasesDTO testCasesDTO;

    /**
     * 公共全局变量
     */
    private Map<String, Object> commonGlobalParams = null;

    /**
     * 全局变量
     */
    private Map<String, Object> globalParams = new HashMap<>();

    /**
     * 同一个testCase重复跑，记录第几次跑
     */
    private String flowShowTitle;

    /**
     * action stack，记录Action深度
     */
    private Stack<ActionSetting> actionSettingsStack = new Stack<>();

    /**
     * 测试报告数据
     */
    private transient TestReportDataTestCaseDTO reportDataContext;

    public TestCasesRunContext() {
    }

    public TestCasesRunContext(TestCasesDTO testCasesDTO) {
        this.testCasesDTO = testCasesDTO;
    }

    public void addGlobalParams(Map<String, Object> addParams) {
        if (MapUtils.isNotEmpty(addParams)) {
            globalParams.putAll(addParams);
        }
    }

    public void addGlobalParams(String key, Object value) {
        if (StringUtils.isNotBlank(key)) {
            globalParams.put(key, value);
        }
    }

    public void pushActionSetting(ActionSetting actionSetting) {
        if (actionSetting == null) {
            return;
        }
        this.actionSettingsStack.push(actionSetting);
    }

    public void popActionSetting() {
        if (CollectionUtils.isEmpty(this.actionSettingsStack)) {
            return;
        }
        this.actionSettingsStack.pop();
    }

}
