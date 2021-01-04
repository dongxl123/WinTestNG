package com.winbaoxian.testng.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 正态分布对象
 */
@Getter
@Setter
public class NormalDistributionDTO implements Serializable {

    /**
     * 样表数量
     */
    private long count;
    /**
     * 平均值
     */
    private double mean;
    /**
     * 标准差
     */
    private double std;
    /**
     * 两点间值换算公式(线性拟合)
     */
    private List<LinearTransformFunction> linearTransformFunctionList;


    @Getter
    @Setter
    public static class LinearTransformFunction implements Serializable {

        /**
         * 目标开始点
         */
        private double targetStart;

        /**
         * 目标结束点
         */
        private double targetEnd;

        /**
         * 正态分布起始点（包含）
         */
        private double normalStart;

        /**
         * 正态分布结束点（不包含）
         */
        private double normalEnd;

        /**
         * 计算表达公式 normalStart<=x<normalEnd
         */
        public double getTargetValue(double x) {
            return targetStart + (targetEnd - targetStart) / (normalEnd - normalStart) * (x - normalStart);
        }

    }

}
