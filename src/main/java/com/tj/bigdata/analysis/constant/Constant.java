package com.tj.bigdata.analysis.constant;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author guoch
 */
public class Constant {
    /**
     * 圆点
     */
    public static final String ROUND_DOT = ".";

    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * redis key 后缀
     */
    public static final String HOUR = "hour";

    /**
     * 一天的时间戳
     */
    public static final Long DAYSTAMPTIMELONG = 86400L;

    /**
     * 一小时的时间戳
     */
    public static final Long HOURSTAMPTIMELONG = 3600L;

    /**
     * 一天的时间戳
     */
    public static final BigDecimal DAYSTAMPTIME = new BigDecimal(String.valueOf(86400));

    /**
     * 一小时的时间戳
     */
    public static final BigDecimal HOURSTAMPTIME = new BigDecimal(String.valueOf(3600));

    /**
     * 一天的时间戳
     */
    public static final BigInteger DAYSTAMPTIMEBIGDECIMAL = new BigInteger(String.valueOf(86400));

    /**
     * 一小时的时间戳
     */
    public static final BigInteger HOURSTAMPTIMEBIGDECIMAL = new BigInteger(String.valueOf(3600));

    /**
     * 一小时的时间戳
     */
    public static final BigDecimal DAYHOURSIZE = new BigDecimal(24);

    /**
     * 一小时的时间戳
     */
    public static final int DAYHOURSIZEINT = 24;

    /**
     * 网络提示信息
     */
    public final static String MESSAGE_NETWORK_UNSTABLE = "您当前网络不稳定，请稍后再试";
}
