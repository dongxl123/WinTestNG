package com.winbaoxian.testng.model.mapper;

import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.dto.TestReportDTO;
import com.winbaoxian.testng.model.entity.TestReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface TestReportMapper {

    TestReportMapper INSTANCE = Mappers.getMapper(TestReportMapper.class);

    TestReportEntity toEntity(TestReportDTO dto);

    TestReportDTO toDTO(TestReportEntity entity);

    @Mappings({
            @Mapping(expression = "java(dto.getTriggerMode().getValue())", target = "triggerMode"),
            @Mapping(expression = "java(dto.getRunState().getValue())", target = "runState")
    })
    TestReportEntity toEntity(TestReportDataSummaryDTO dto);
}
