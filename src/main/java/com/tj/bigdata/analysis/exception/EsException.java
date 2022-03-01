package com.tj.bigdata.analysis.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ES信息异常类
 * @author guoch
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EsException extends RuntimeException{
    private String message;

    public EsException(String message){
        this.message = message;
    }
}
