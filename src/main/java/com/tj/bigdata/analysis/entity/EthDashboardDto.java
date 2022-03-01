package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *  以太坊统计出惨 service dto
 *
 * @author guoch
 */
@Accessors(chain = true)
@Data
public class EthDashboardDto {

    private Map<BigDecimal, LineChartResultValue> points = new HashMap<>();

    @ApiModelProperty(value = "总计 总交易额")
    private BigDecimal txValue;

    @ApiModelProperty(value = "总计 交易次数")
    private Integer txTimes;

    @ApiModelProperty(value = "总计 地址个数")
    private Integer players;
}
