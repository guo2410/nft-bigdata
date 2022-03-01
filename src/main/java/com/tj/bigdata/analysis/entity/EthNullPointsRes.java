package com.tj.bigdata.analysis.entity;

import io.swagger.annotations.ApiModelProperty;
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
public class EthNullPointsRes {

    /**
     * 空map
     */
    private Map<BigDecimal,LineChartResultValue> points = new HashMap<>();
}
