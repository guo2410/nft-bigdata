package com.tj.bigdata.analysis.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TimeRange{

    /**
     * day
     */
    DAY(1, "天",24),

    WEEK(2, "周",7),

    MONTH(3, "月",30),

    QUARTER(4, "季",90),

    YEAR(5, "年",365),

    UNKNOWN(-1, "未知",0);

    private final Integer code;

    private final String value;

    private final Integer interval;



    /**
     *     自定义反序列函数
     *     JsonCreator.Mode.DELEGATING： 接收单个值，将接收的值整个传入自定义函数
     * @param code
     * @return
     */
    public static TimeRange get(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        return Arrays.stream(TimeRange.values()).filter(i -> i.getCode() == code.intValue()).findAny().orElse(UNKNOWN);
    }
}
