package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@ApiModel(value = "大数据 以太坊 - 24小时余额数据 - 出参")
@Accessors(chain = true)
@Data
public class EthDashboardHourBalanceRes implements Serializable {

    @ApiModelProperty(value = "点格式mao key为时间点，value为统计值")
    private Map<BigDecimal, BigDecimal> points = new HashMap<>();

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
}
