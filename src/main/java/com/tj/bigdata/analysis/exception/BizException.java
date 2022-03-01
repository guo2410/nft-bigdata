package com.tj.bigdata.analysis.exception;

/**
 * @author guoch
 */
public class BizException extends RuntimeException {
    private static final String SPLIT_MARK = "&&&";
    private static final long serialVersionUID = -7727955365030947445L;
    private String subCode;
    private String subMessage;

    public BizException(String subCode, String subMessage) {
        super(subCode + "&&&" + subMessage);
        this.subCode = subCode;
        this.subMessage = subMessage;
    }

    public String getSubCode() {
        return this.subCode;
    }

    public String getSubMessage() {
        return this.subMessage;
    }

    @Override
    public String toString() {
        return "BizException(subCode=" + this.getSubCode() + ", subMessage=" + this.getSubMessage() + ")";
    }
}
