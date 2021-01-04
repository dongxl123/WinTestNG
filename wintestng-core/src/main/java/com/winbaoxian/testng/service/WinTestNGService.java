package com.winbaoxian.testng.service;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.enums.RunState;
import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.dto.ActionTemplateDTO;
import com.winbaoxian.testng.model.dto.ResourceConfigDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.model.entity.*;
import com.winbaoxian.testng.model.mapper.*;
import com.winbaoxian.testng.repository.*;
import com.winbaoxian.testng.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 17:20
 */

@Service
@Slf4j
public class WinTestNGService {

    @Resource
    private WinTestNGRepository winTestNGRepository;
    @Resource
    private ResourceConfigRepository resourceConfigRepository;
    @Resource
    private GlobalParamsRepository globalParamsRepository;
    @Resource
    private ActionTemplateRepository actionTemplateRepository;
    @Resource
    private ModuleRepository moduleRepository;
    @Resource
    private ProjectRepository projectRepository;
    @Resource
    private TestCasesRepository testCasesRepository;
    @Resource
    private TestReportRepository testReportRepository;
    @Resource
    private TestReportDetailsRepository testReportDetailsRepository;
    @Resource
    private SysSettingsRepository sysSettingsRepository;
    @Resource
    private EmailSenderService emailSenderService;


    public String getSysValueByKey(String key) {
        SysSettingsEntity sysSettings = sysSettingsRepository.findByKeyAndDeletedFalse(key);
        if (sysSettings == null) {
            return null;
        }
        return sysSettings.getValue();
    }

    public TestCasesDTO[] getTestCasesList(String sql) {
        List<TestCasesEntity> entityList = winTestNGRepository.getTestCasesList(sql);
        return TestCasesMapper.INSTANCE.toDTOArray(entityList);
    }

    public Map<String, Object> getGlobalParameters() {
        List<GlobalParamsEntity> globalParamsEntityList = globalParamsRepository.findByDeletedFalse();
        if (CollectionUtils.isEmpty(globalParamsEntityList)) {
            return null;
        }
        return globalParamsEntityList.stream().collect(Collectors.toMap(o -> o.getKey(), o -> o.getValue(), (k, v) -> v));
    }

    public ResourceConfigDTO getResourceConfig(Long id) {
        ResourceConfigEntity entity = resourceConfigRepository.findOne(id);
        return ResourceConfigMapper.INSTANCE.toDTO(entity);
    }

    public ActionTemplateDTO getActionTemplate(Long id) {
        ActionTemplateEntity entity = actionTemplateRepository.findOne(id);
        return ActionTemplateMapper.INSTANCE.toDTO(entity);
    }

    public Date getLastAutomatedTestingRuntimeByProjectId(Long projectId) {
        return winTestNGRepository.getLastAutomatedTestingRuntimeByProjectId(projectId);
    }

    @Transactional
    public void saveTestReportData(TestReportDataSummaryDTO summaryData) {
        if (summaryData == null || CollectionUtils.isEmpty(summaryData.getTestCaseDataList())) {
            return;
        }
        //保存汇总数据
        TestReportEntity testReportEntity = TestReportMapper.INSTANCE.toEntity(summaryData);
        testReportRepository.save(testReportEntity);
        //保存明细数据
        if (CollectionUtils.isEmpty(summaryData.getTestCaseDataList())) {
            return;
        }
        List<TestReportDetailsEntity> entityList = TestReportDetailsMapper.INSTANCE.toEntityList(summaryData.getTestCaseDataList());
        testReportDetailsRepository.save(entityList);
        //更新测试任务最后运行状态字段
        Map<Long, RunState> idStateMap = summaryData.getTestCaseDataList().stream().collect(Collectors.toMap(o -> o.getTestCasesId(), o -> o.getRunState()));
        if (MapUtils.isEmpty(idStateMap)) {
            return;
        }
        List<TestCasesEntity> testCasesEntityList = testCasesRepository.findByIdIn(new ArrayList<>(idStateMap.keySet()));
        if (CollectionUtils.isEmpty(testCasesEntityList)) {
            return;
        }
        for (TestCasesEntity entity : testCasesEntityList) {
            RunState runState = idStateMap.get(entity.getId());
            entity.setLastRunState(runState.getValue());
        }
        testCasesRepository.save(testCasesEntityList);
    }

    public void sendReportEmail(TestReportDataSummaryDTO summaryData, String reportUrl) {
        try {
            log.info("start sendReportEmail, reportUuid: {}", summaryData.getReportUuid());
            Long projectId = summaryData.getProjectId();
            if (projectId == null) {
                return;
            }
            ProjectEntity project = projectRepository.findOne(projectId);
            if (StringUtils.isBlank(project.getMailAddress())) {
                log.info("project has not config mailAddress, projectId:{}", project.getId());
                return;
            }
            String title = String.format(WinTestNGConstant.REPORT_EMAIL_TITLE, summaryData.getTriggerMode().getTitle(), project.getName(), RunState.SUCCESS.equals(summaryData.getRunState()) ? "成功" : "失败");
            String[] tosArray = StringUtils.split(project.getMailAddress(), WinTestNGConstant.CHAR_COMMA);
            String content = getReportEmailContent(reportUrl);
            emailSenderService.sendMailSync(title, content, Arrays.asList(tosArray));
        } catch (Exception e) {
            log.error("sendReportEmail failed", e);
        }
    }

    private String getReportEmailContent(String reportUrl) {
        String summaryHtml = HttpUtils.INSTANCE.doGet(reportUrl);
        String reportUrlContent = String.format("<div class=\"container small\" style=\"margin-bottom:3em\"><h2 class=\"sub-header\">报告地址</h2><div><a href=\"%s\">%s</a></div></div>", reportUrl, reportUrl);
        String defaultContent = String.format("<html><body>%s</body></html>", reportUrlContent);
        if (StringUtils.isBlank(summaryHtml)) {
            return defaultContent;
        }
        try {
            Document document = Jsoup.parse(summaryHtml);
            document.body().getElementsByClass("container").first().before(reportUrlContent);
            return document.html();
        } catch (Exception e) {
            log.error("getReportEmailContent error, reportUrl :{}", reportUrl);
        }
        return defaultContent;
    }

}
