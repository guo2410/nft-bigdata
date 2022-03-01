package com.tj.bigdata.analysis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author guoch
 */
@Data
@Accessors(chain = true)
public class HourRedisCache implements Serializable {

    private int txTimes;

    private Double value;

    private BigDecimal balance;

    private Set<String> fromList = new HashSet<>();
}
