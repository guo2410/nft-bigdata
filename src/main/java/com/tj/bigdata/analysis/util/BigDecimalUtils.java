package com.tj.bigdata.analysis.util;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author guoch
 */
public class BigDecimalUtils {
    private static final int DEF_DIV_SCALE = 2;

    private BigDecimalUtils() {
    }

    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    public static BigDecimal mul(Integer v1, Integer v2) {
        return (new BigDecimal(v1)).multiply(new BigDecimal(v2));
    }

    public static BigDecimal mul(BigDecimal v1, Integer v2) {
        return v1.multiply(new BigDecimal(v2));
    }

    public static BigDecimal mul(BigDecimal v1, Long v2) {
        return v1.multiply(new BigDecimal(v2));
    }

    public static BigDecimal mul(Double v1, Double v2) {
        return (new BigDecimal(Double.toString(v1))).multiply(new BigDecimal(Double.toString(v2)));
    }

    public static BigDecimal mul(BigDecimal v1, Double v2) {
        return v1.multiply(new BigDecimal(Double.toString(v2)));
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    public static BigDecimal mul(Double v1, Double v2, Double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.multiply(b2).multiply(b3);
    }

    public static BigDecimal mul(BigDecimal v1, Double v2, Double v3) {
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return v1.multiply(b2).multiply(b3);
    }

    public static BigDecimal div(Double v1, Double v2) {
        return div(v1, v2, 2);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            return v1.divide(v2, scale, 4);
        }
    }

    public static BigDecimal div(Integer v1, Integer v2) {
        return (new BigDecimal(v1)).divide(new BigDecimal(v2), 2, 4);
    }

    public static BigDecimal div(BigDecimal v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            return v1.divide(new BigDecimal(v2), scale, 4);
        }
    }

    public static BigDecimal div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, RoundingMode.HALF_UP);
        }
    }

    public static BigDecimal round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            return b.divide(BigDecimal.ONE, scale, 4);
        }
    }

    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            return v.divide(BigDecimal.ONE, scale, 4);
        }
    }

    public static BigDecimal floor(double v, Integer... scale) {
        if (ArrayUtils.isEmpty(scale)) {
            return (new BigDecimal(Double.toString(v))).setScale(0, RoundingMode.DOWN);
        } else if (scale[0] < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            return (new BigDecimal(Double.toString(v))).setScale(scale[0], RoundingMode.DOWN);
        }
    }

    public static BigDecimal floor(BigDecimal v, Integer... scale) {
        if (ArrayUtils.isEmpty(scale)) {
            return v.setScale(0, RoundingMode.DOWN);
        } else if (scale[0] < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            return v.setScale(scale[0], RoundingMode.DOWN);
        }
    }

    public static BigDecimal ceil(BigDecimal v, Integer... scale) {
        if (ArrayUtils.isEmpty(scale)) {
            return v.setScale(0, RoundingMode.UP);
        } else if (scale[0] < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            return v.setScale(scale[0], RoundingMode.UP);
        }
    }

    public static boolean eq(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) == 0;
    }

    public static boolean ne(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) != 0;
    }

    public static boolean gt(double v1, double v2) {
        return gt(new BigDecimal(Double.toString(v1)), new BigDecimal(Double.toString(v2)));
    }

    public static boolean gte(double v1, double v2) {
        return gte(new BigDecimal(Double.toString(v1)), new BigDecimal(Double.toString(v2)));
    }

    public static boolean lt(double v1, double v2) {
        return lt(new BigDecimal(Double.toString(v1)), new BigDecimal(Double.toString(v2)));
    }

    public static boolean lte(double v1, double v2) {
        return lte(new BigDecimal(Double.toString(v1)), new BigDecimal(Double.toString(v2)));
    }

    public static boolean gt(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) > 0;
    }

    public static boolean gte(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) >= 0;
    }

    public static boolean lt(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) < 0;
    }

    public static boolean lte(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2) <= 0;
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(String.valueOf(0.7D)));
        System.out.println(mul(new BigDecimal(100), new BigDecimal(String.valueOf(0.7D))));
    }
}
