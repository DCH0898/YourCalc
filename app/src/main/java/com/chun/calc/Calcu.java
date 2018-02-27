package com.chun.calc;

/**
 * 计算的逻辑
 * Created by Dachun on 2017/8/7.
 */

public class Calcu {
    /**
     * 获取市值 净值 * 份额
     *
     * @param netValue 净值
     * @param share    份额
     * @return 市值
     */
    public static double getValue(double netValue, double share) {
        return netValue * share;
    }

    /**
     * 获取盈亏额 持有市值 - 市值
     *
     * @param total 总额
     * @param value 市值
     * @return 盈亏额
     */
    public static double getProfit(double total, double value) {
        return value - total;
    }


    /**
     * 获取买卖份额
     *
     * @param profit   盈亏额
     * @param netValue 净值
     * @return 买卖份额
     */
    public static double getBayShare(double profit, double netValue) {
        return profit / netValue;
    }


    /**
     * 获取估值
     *
     * @param value      市值
     * @param profit     盈亏额
     * @param have_share 持有份额
     * @return 估值
     */
    public static double getValuation(double value, double profit, double have_share) {
        return (value - profit) / have_share;
    }
}