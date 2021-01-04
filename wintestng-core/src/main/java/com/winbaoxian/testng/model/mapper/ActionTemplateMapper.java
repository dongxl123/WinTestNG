package com.winbaoxian.testng.model.mapper;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.testng.model.dto.ActionTemplateDTO;
import com.winbaoxian.testng.model.entity.ActionTemplateEntity;
import com.winbaoxian.testng.utils.ConfigParseUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring", imports = {JSON.class, ConfigParseUtils.class})
public interface ActionTemplateMapper {

    ActionTemplateMapper INSTANCE = Mappers.getMapper(ActionTemplateMapper.class);

    @Mappings({
            @Mapping(expression = "java(ConfigParseUtils.INSTANCE.parseActionSettingList(entity.getActions()))", target = "actions")
    })
    ActionTemplateDTO toDTO(ActionTemplateEntity entity);

    ActionTemplateDTO[] toDTOArray(List<ActionTemplateEntity> entityList);

    @Mappings({
            @Mapping(expression = "java(JSON.toJSONString(dto.getActions()))", target = "actions")
    })
    ActionTemplateEntity toEntity(ActionTemplateDTO dto);

}
