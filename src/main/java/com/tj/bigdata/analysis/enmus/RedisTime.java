package com.tj.bigdata.analysis.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

/**
 * @author guoch
 */

@AllArgsConstructor
public enum RedisTime {

    /**
     * 计算类型 1
     */
    HOUR(1, "小时",new BigInteger(String.valueOf(90000))),

    /**
     * 计算类型 2
     */
    DAY(2, "天",new BigInteger(String.valueOf(31708800)));

    @Getter
    private final Integer code;

    @Getter
    private final String value;

    @Getter
    private final BigInteger range;
}

