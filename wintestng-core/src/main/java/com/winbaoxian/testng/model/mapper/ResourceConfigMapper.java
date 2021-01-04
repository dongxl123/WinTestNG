package com.winbaoxian.testng.model.mapper;

import com.winbaoxian.testng.model.dto.ResourceConfigDTO;
import com.winbaoxian.testng.model.entity.ResourceConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface ResourceConfigMapper {

    ResourceConfigMapper INSTANCE = Mappers.getMapper(ResourceConfigMapper.class);

    ResourceConfigDTO toDTO(ResourceConfigEntity entity);

}
