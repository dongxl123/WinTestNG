package com.winbaoxian.testng.model.mapper;

import com.winbaoxian.testng.model.entity.ProjectQualityReportHistoryEntity;
import com.winbaoxian.testng.model.dto.ProjectQualityReportHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 项目质量报告(ProjectQualityReportHistory)Mapper类
 *
 * @author dongxuanliang252
 * @date 2020-06-24 16:52:19
 */
@Mapper(componentModel = "spring")
public interface ProjectQualityReportHistoryMapper {
    
    ProjectQualityReportHistoryMapper INSTANCE = Mappers.getMapper(ProjectQualityReportHistoryMapper.class);

    ProjectQualityReportHistoryDTO toDTO(ProjectQualityReportHistoryEntity entity);

    ProjectQualityReportHistoryEntity toEntity(ProjectQualityReportHistoryDTO dto);
    
    List<ProjectQualityReportHistoryDTO> toDTOList(List<ProjectQualityReportHistoryEntity> entityList);

}