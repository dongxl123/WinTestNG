package com.winbaoxian.testng.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

}
