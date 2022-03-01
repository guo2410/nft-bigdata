package com.tj.bigdata.analysis.enmus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TokenType {

    ETH(1, "eth"),

    /**
     * error
     */
    UNKNOWN(-1, "未知");

    private final Integer code;

    private final String value;

    /**
     * 自定义反序列函数
     * JsonCreator.Mode.DELEGATING： 接收单个值，将接收的值整个传入自定义函数
     *
     * @param code
     * @return
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TokenType get(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        return Arrays.stream(TokenType.values()).filter(i -> i.getCode() == code.intValue()).findAny().orElse(UNKNOWN);

    }
}
