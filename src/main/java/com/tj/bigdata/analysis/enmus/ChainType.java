package com.tj.bigdata.analysis.enmus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author guoch
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
public enum ChainType {

    /**
     * <h1>以太坊类型</h1>
     */
    ETHEREUM(1, "以太坊"),

    UNKNOWN(-1, "未知");

    @Getter
    private final Integer code;

    @Getter
    private final String value;

    /**
     *     自定义反序列函数
     *     JsonCreator.Mode.DELEGATING： 接收单个值，将接收的值整个传入自定义函数
     * @param code
     * @return
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ChainType get(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        return Arrays.stream(ChainType.values()).filter(i -> i.getCode() == code.intValue()).findAny().orElse(UNKNOWN);

    }
}
