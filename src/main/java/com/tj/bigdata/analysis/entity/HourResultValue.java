package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author guoch
 */
@Accessors(chain =  true)
@Data
public class HourResultValue {

    @ApiModelProperty(value = "1小时 总交易额")
    private Double txValue;

    @ApiModelProperty(value = "1小时 交易次数")
    private Integer txTimes;

    @ApiModelProperty(value = "1小时 地址个数")
    private Integer players;

    @ApiModelProperty(value = "1小时 合约余额")
    private Integer balance;
}
