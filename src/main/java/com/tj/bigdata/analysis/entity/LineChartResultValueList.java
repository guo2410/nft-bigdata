package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author guoch
 */
@Accessors(chain = true)
@Data
public class LineChartResultValueList {

    @ApiModelProperty(value = "总交易额")
    private CopyOnWriteArrayList<BigDecimal> txValue = new CopyOnWriteArrayList<>();

    @ApiModelProperty(value = "交易次数")
    private CopyOnWriteArrayList<Integer> txTime = new CopyOnWriteArrayList<>();

    @ApiModelProperty(value = "玩家数")
    private CopyOnWriteArrayList<Integer> players = new CopyOnWriteArrayList<>();

    @ApiModelProperty(value = "玩家数")
    private Set<String> playersSet = new HashSet<>();

}
