package com.tj.bigdata.analysis.exception;

import com.tj.bigdata.analysis.util.ReflexHandler;

/**
 * <h1>业务异常</h1>
 *
 * @author guoch
 */
public class BusinessException extends RuntimeException {

    private String subCode;

    private String subMessage;

    public BusinessException() {

    }

    public <R> BusinessException(R result) {
        super(ReflexHandler.get(result, "subCode", String.class) + "---" + ReflexHandler.get(result, "subMessage", String.class));
        this.subCode = ReflexHandler.get(result, "subCode", String.class);
        this.subMessage = ReflexHandler.get(result, "subMessage", String.class);

    }

    public BusinessException(String s1, String s2) {
        super(s1 + "---" + s2);
        this.subCode = s1;
        this.subMessage = s2;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }
}
