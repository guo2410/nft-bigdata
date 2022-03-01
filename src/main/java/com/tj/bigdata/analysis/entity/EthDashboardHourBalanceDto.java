package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Bidi;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Accessors(chain = true)
@Data
public class EthDashboardHourBalanceDto implements Serializable {

    /**
     * 点格式mao key为时间点，value为统计值
     */
    private Map<BigDecimal, BigDecimal> points = new HashMap<>();

    private BigDecimal balance;
}
