package com.tj.bigdata.analysis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author guoch
 */
@Accessors(chain = true)
@Data
public class EthNullPointsReq {

    /**
     * 类型  timeRange
     */
    private Integer type;

    /**
     *  day时间范围
     */
    private Integer range;
}
