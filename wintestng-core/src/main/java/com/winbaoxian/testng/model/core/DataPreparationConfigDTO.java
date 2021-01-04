package com.winbaoxian.testng.model.core;

import com.winbaoxian.testng.enums.DataPreparationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 14:04
 */
@Setter
@Getter
public class DataPreparationConfigDTO implements Serializable {

    private DataPreparationType type;

    /**
     * 显示的标题，用于log中，支持freemarker
     */
    private String showTitle;

    /**
     * DataPreparationType.csv
     */
    private String fileName;
    private Character separator;
    private List<String> fieldNameList;

    /**
     * DataPreparationType.text
     */
    private String text;

    /**
     * DataPreparationType.mysql
     */
    private Long resourceId;
    private String sql;
}
