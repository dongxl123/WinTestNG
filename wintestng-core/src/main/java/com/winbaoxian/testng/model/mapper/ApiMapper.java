package com.winbaoxian.testng.model.mapper;

import com.winbaoxian.testng.model.entity.ApiEntity;
import com.winbaoxian.testng.model.dto.ApiDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * (Api)Mapper类
 *
 * @author dongxuanliang252
 * @date 2020-05-25 15:08:03
 */
@Mapper(componentModel = "spring")
public interface ApiMapper {
    
    ApiMapper INSTANCE = Mappers.getMapper(ApiMapper.class);

    ApiDTO toDTO(ApiEntity entity);

    ApiEntity toEntity(ApiDTO dto);
    
    List<ApiDTO> toDTOList(List<ApiEntity> entityList);

}