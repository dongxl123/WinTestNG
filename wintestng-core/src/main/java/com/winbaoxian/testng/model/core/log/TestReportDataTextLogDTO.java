package com.winbaoxian.testng.model.core.log;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author dongxuanliang252
 * @date 2019-03-26 16:09
 */
@Getter
@Setter
public class TestReportDataTextLogDTO implements TestReportDataBaseLogDTO {

    /**
     * 时间
     */
    private Date time;
    /**
     * 日志
     */
    private String text;

}
