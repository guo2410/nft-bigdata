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
public class RedisCache implements Serializable {
    private int txTimes;
    private BigDecimal value;
    private Set<String> fromList = new HashSet<>();
}
