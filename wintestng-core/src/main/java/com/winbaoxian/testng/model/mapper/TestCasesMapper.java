package com.winbaoxian.testng.model.mapper;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.ParamConfigDTO;
import com.winbaoxian.testng.model.core.action.normal.AssertVerifyConfigDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.model.entity.TestCasesEntity;
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
@Mapper(componentModel = "spring", imports = {JSON.class, ParamConfigDTO.class, ConfigParseUtils.class, AssertVerifyConfigDTO.class, DataPreparationConfigDTO.class})
public interface TestCasesMapper {

    TestCasesMapper INSTANCE = Mappers.getMapper(TestCasesMapper.class);

    @Mappings({
            @Mapping(expression = "java(JSON.parseObject(entity.getDataPreparationConfig(), DataPreparationConfigDTO.class))", target = "dataPreparationConfig"),
            @Mapping(expression = "java(ConfigParseUtils.INSTANCE.parseActionSettingList(entity.getPreActions()))", target = "preActions"),
            @Mapping(expression = "java(ConfigParseUtils.INSTANCE.parseActionSettingList(entity.getMainActions()))", target = "mainActions"),
            @Mapping(expression = "java(ConfigParseUtils.INSTANCE.parseActionSettingList(entity.getPostActions()))", target = "postActions"),
    })
    TestCasesDTO toDTO(TestCasesEntity entity);

    TestCasesDTO[] toDTOArray(List<TestCasesEntity> entityList);

    List<TestCasesDTO> toDTOList(List<TestCasesEntity> entityList);

    @Mappings({
            @Mapping(expression = "java(dto.getDataPreparationConfig()==null?null:JSON.toJSONString(dto.getDataPreparationConfig()))", target = "dataPreparationConfig"),
            @Mapping(expression = "java(dto.getPreActions()==null?null:JSON.toJSONString(dto.getPreActions()))", target = "preActions"),
            @Mapping(expression = "java(dto.getMainActions()==null?null:JSON.toJSONString(dto.getMainActions()))", target = "mainActions"),
            @Mapping(expression = "java(dto.getPostActions()==null?null:JSON.toJSONString(dto.getPostActions()))", target = "postActions"),
    })
    TestCasesEntity toEntity(TestCasesDTO dto);

}
