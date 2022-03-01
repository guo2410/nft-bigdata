package com.tj.bigdata.analysis.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ES信息异常类
 * @author guoch
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EthException extends RuntimeException{
    private String message;

    public EthException(String message){
        this.message = message;
    }
}
