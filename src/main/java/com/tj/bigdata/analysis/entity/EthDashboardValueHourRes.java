package com.tj.bigdata.analysis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tj.bigdata.analysis.MyAnnotation.BigDecimalSerializer;
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
@ApiModel(value = "大数据 以太坊 - 24小时数据 - 出参")
@Accessors(chain = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EthDashboardValueHourRes implements Serializable {

    @ApiModelProperty(value = "点格式mao key为时间点，value为统计值")
    private Map<BigDecimal, LineChartResultValue> points = new HashMap<>();

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "type为1时：24小时单点 最新节点 type为2时:总计")
    private BigDecimal txValue;

    @ApiModelProperty(value = "type为1时：24小时单点 最新节点 type为2时:总计")
    private Integer txTimes;

    @ApiModelProperty(value = "type为1时：24小时单点 最新节点 type为2时:总计")
    private Integer players;
}
