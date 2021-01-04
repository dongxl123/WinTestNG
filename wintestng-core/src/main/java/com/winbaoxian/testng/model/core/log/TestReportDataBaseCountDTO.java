package com.winbaoxian.testng.model.core.log;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dongxuanliang252
 * @date 2019-03-26 16:10
 */
@Getter
@Setter
public abstract class TestReportDataBaseCountDTO implements Serializable {

    /**
     * 报告唯一UID
     */
    private String reportUuid;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 花费的时间(单位毫秒)
     */
    private Long duration;

    /**
     * 数据驱动时，总数
     */
    private long totalCount;

    /**
     * 数据驱动时，成功数
     */
    private long successCount;

    /**
     * 数据驱动时，失败数
     */
    private long failCount;


    public synchronized void incSuccessCount() {
        successCount++;
    }

    public synchronized void incFailCount() {
        failCount++;
    }

    public Long getDuration() {
        if (startTime != null && endTime != null) {
            return endTime.getTime() - startTime.getTime();
        }
        return duration;
    }
}
