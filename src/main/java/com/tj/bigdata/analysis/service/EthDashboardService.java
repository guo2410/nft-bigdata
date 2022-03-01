package com.tj.bigdata.analysis.service;

import com.tj.bigdata.analysis.entity.*;

/**
 * 以太坊value数据统计
 *
 * @author guoch
 */
public interface EthDashboardService {
    /**
     * 折线统计数据 异步
     *
     * @param ethDashboardValueReq 入参
     * @return map
     */
    EthDashboardDto listLineChartRangeSync(EthDashboardValueReq ethDashboardValueReq);

    /**
     * 工厂获取24小时数据
     *
     * @param ethDashboardValueReq 请求类型
     * @return 类型节点
     */
    EthDashboardHourDto hourFromFactory(EthDashboardValueHourReq ethDashboardValueReq);

    /**
     * 获取24小时余额数据
     *
     * @param ethDashboardValueReq 入参
     * @return map
     */
    EthDashboardHourBalanceDto listLineChartRangeBalance(EthDashboardValueReq ethDashboardValueReq);

    /**
     * 生成空map
     *
     * @param ethNullPointsReq 入参
     * @return 返回
     */
    EthNullPointsRes listNullPointsModel(EthNullPointsReq ethNullPointsReq);
}
