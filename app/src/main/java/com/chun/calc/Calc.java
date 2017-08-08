package com.chun.calc;

/**
 * 计算的逻辑
 * Created by Dachun on 2017/8/7.
 */

public class Calc {
    /**
     * 获取市值 单价 * 份额
     *
     * @param unit_price 单价
     * @param share      份额
     * @return 市值
     */
    public static double getValue(double unit_price, double share) {
        return unit_price * share;
    }

    /**
     * 获取更新市值 估值 * 份额
     *
     * @param valuation 估值
     * @param share     份额
     * @return 更新市值
     */
    public static double getNewValue(double valuation, double share) {
        return valuation * share;
    }

    /**
     * 获取持有市值 估值 * 持有份额
     *
     * @param valuation  估值
     * @param have_share 持有份额
     * @return 持有市值
     */
    public static double getHaveValue(double valuation, double have_share) {
        return valuation * have_share;
    }

    /**
     * 获取盈亏额 持有市值 - 市值
     *
     * @param have_value 持有市值
     * @param value      市值
     * @return 盈亏额
     */
    public static double getProfit(double have_value, double value) {
        return have_value - value;
    }

    /**
     * 获取涨幅 （估值 - 单价） / 单价
     *
     * @param valuation  估值
     * @param unit_price 单价
     * @return 涨幅
     */
    public static double getIncrease(double valuation, double unit_price) {
        return (valuation - unit_price) / unit_price * 100;
    }


    /**
     * 获取买卖份额
     *
     * @param new_value 新市值
     * @param value     市值
     * @param valuation 估值
     * @return 买卖份额
     */
    public static double getBayShare(double new_value, double value, double valuation) {
        return (value - new_value) / valuation;
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
