package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *  以太坊24小时数据统计出惨 service dto
 *
 * @author guoch
 */
@Accessors(chain = true)
@Data
public class EthDashboardHourDto implements Serializable {

    private Map<BigDecimal, LineChartResultValue> points = new HashMap<>();

    /**
     * 交易额
     */
    private BigDecimal txValue;

    /**
     * 交易次数
     */
    private Integer txTimes;

    /**
     * 地址个数
     */
    private Integer players;
}
