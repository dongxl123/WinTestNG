package com.winbaoxian.testng.model.mapper;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.testng.model.core.log.TestReportDataStepLogDTO;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.TestReportDetailsDTO;
import com.winbaoxian.testng.model.entity.TestReportDetailsEntity;
import org.apache.commons.collections.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring", imports = {JSON.class, List.class, TestReportDataStepLogDTO.class, CollectionUtils.class})
public interface TestReportDetailsMapper {

    TestReportDetailsMapper INSTANCE = Mappers.getMapper(TestReportDetailsMapper.class);

    @Mappings({
            @Mapping(expression = "java(CollectionUtils.isEmpty(dto.getDetails())?null:JSON.toJSONString(dto.getDetails()))", target = "details"),
            @Mapping(expression = "java(CollectionUtils.isEmpty(dto.getExceptions())?null:JSON.toJSONString(dto.getExceptions()))", target = "exceptions")
    })
    TestReportDetailsEntity toEntity(TestReportDetailsDTO dto);

    @Mappings({
            @Mapping(expression = "java(JSON.parseArray(entity.getDetails(), TestReportDataStepLogDTO.class))", target = "details"),
            @Mapping(expression = "java(JSON.parseArray(entity.getExceptions(), String.class))", target = "exceptions")
    })
    TestReportDetailsDTO toDTO(TestReportDetailsEntity entity);

    List<TestReportDetailsDTO> toDTOList(List<TestReportDetailsEntity> entityList);

    @Mappings({
            @Mapping(expression = "java(dto.getRunState().getValue())", target = "runState"),
            @Mapping(expression = "java(CollectionUtils.isEmpty(dto.getStepDataList())?null:JSON.toJSONString(dto.getStepDataList()))", target = "details"),
            @Mapping(expression = "java(CollectionUtils.isEmpty(dto.getErrorMessages())?null:JSON.toJSONString(dto.getErrorMessages()))", target = "exceptions")
    })
    TestReportDetailsEntity toEntity(TestReportDataTestCaseDTO dto);

    List<TestReportDetailsEntity> toEntityList(List<TestReportDataTestCaseDTO> dtoList);
}
