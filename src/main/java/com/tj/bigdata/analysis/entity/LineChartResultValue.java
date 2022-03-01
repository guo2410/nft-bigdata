package com.tj.bigdata.analysis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tj.bigdata.analysis.MyAnnotation.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author guoch
 */
@Accessors(chain = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineChartResultValue {

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "总交易额")
    private BigDecimal txValue = new BigDecimal(0);

    @ApiModelProperty(value = "交易次数")
    private int txTimes;

    @ApiModelProperty(value = "玩家数")
    private int players;

    @ApiModelProperty(value = "玩家数")
    private Set<String> playersSet = new HashSet<>();

}
