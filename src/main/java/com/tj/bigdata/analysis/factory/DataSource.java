package com.tj.bigdata.analysis.factory;

import com.tj.bigdata.analysis.entity.EthDashboardDto;
import com.tj.bigdata.analysis.entity.EthDashboardValueHourReq;
import org.springframework.stereotype.Component;

/**
 * 创建24小时数据方式
 *
 * @author guoch
 */
@Component
public interface DataSource {

    /**
     * 抽象方法
     *
     * @param ethDashboardValueHourReq 入参
     * @return 结果
     */
    EthDashboardDto structPoints(EthDashboardValueHourReq ethDashboardValueHourReq);
}
