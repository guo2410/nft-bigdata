package com.tj.bigdata.analysis.exception;

import com.tj.bigdata.analysis.util.ResponseData;

/**
 * @author guoch
 */
public class IllegalArgumentsException extends IllegalArgumentException{

    public IllegalArgumentsException(String s1, String s2) {
        super(s1 + ResponseData.SPLIT_MARK + s2);
    }
}
