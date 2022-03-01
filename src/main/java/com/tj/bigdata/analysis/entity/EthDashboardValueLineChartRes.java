package com.tj.bigdata.analysis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guoch
 */
@ApiModel(value = "大数据 以太坊 - 折线图 - 出参")
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EthDashboardValueLineChartRes implements Serializable {

    @ApiModelProperty(value = "点格式mao key为时间点，value为统计值")
    private Map<BigDecimal, LineChartResultValue> points = new HashMap<>();

    @ApiModelProperty(value = "总计 总交易额")
    private BigDecimal txValue;

    @ApiModelProperty(value = "总计 交易次数")
    private Integer txTimes;

    @ApiModelProperty(value = "总计 地址个数")
    private Integer players;

}
