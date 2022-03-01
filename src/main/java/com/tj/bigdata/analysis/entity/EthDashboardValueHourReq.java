package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author guoch
 */
@ApiModel(value = "大数据 以太坊 - 值对象 - 请求")
@Data
public class EthDashboardValueHourReq implements Serializable {

    @NotNull(message = "链类型不能为空")
    @ApiModelProperty(value = "编号链类型 1：以太坊")
    private Integer chain;

    @ApiModelProperty(value = "token类型 1：eth")
    private Integer token;

    @NotNull(message = "appId不能为空")
    @ApiModelProperty(value = "dApp编号")
    private Integer appId;

    @NotNull(message = "type不能为空")
    @ApiModelProperty(value = "1：24小时实时 2：24小时 昨天")
    private Integer type;

    @ApiModelProperty(value = "查询合约地址集合")
    private List<String> toAddressArray = new ArrayList<>();

    @NotNull(message = "时间区间不能为空")
    @ApiModelProperty(value = "时间区间 1：24小时,2：周,3：月,4：年 (24小时/实时数据和余额为1)")
    private Integer timeRange;
}
