package com.tj.bigdata.analysis;

import com.tj.bigdata.analysis.util.DateUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TimeTest {
    public static void main(String[] args) {

        Long currDayTime = DateUtil.getCurrDayTime();
        Long currHourTime = DateUtil.getCurrHourTime();
        System.out.println(currDayTime);
        System.out.println(currHourTime);
//        long dateByTime = DateUtil.getDateByTime();
//        System.out.println(dateByTime);
//        String s = DateUtil.timeStampToDate("1613769677", DateUtil.DEFAULT_DATE_TIME_SPECIAL);
//        System.out.println(s);
//        Set hs = new HashSet();
//        hs.add("111");
//        hs.add("222");
//        hs.add("333");
//        hs.add("444");
//
//        LineChartResultValue lineChartResultValue = new LineChartResultValue();
//        lineChartResultValue.setPlayersSet(hs);
//
//
//        System.out.println(lineChartResultValue.getPlayersSet().size());
//
//        lineChartResultValue.getPlayersSet().add("11");
//        lineChartResultValue.getPlayersSet().add("11");
//        System.out.println(lineChartResultValue.getPlayersSet().size());
//        BigInteger bigInteger = new BigInteger("1614969326");
//        BigInteger bigInteger1 = new BigInteger("86400");
//        System.out.println(DateUtil.timeStampHour(bigInteger));
//        System.out.println(DateUtil.timeStampDay(bigInteger));
//        BigDecimal timeStamp = new BigDecimal("1615287600");
//        BigInteger bigInteger = timeStamp.toBigInteger();
//        Long aLong = DateUtil.timeStampDay(bigInteger);
//        Long aLong1 = DateUtil.timeStampHour(bigInteger);
//        Long res = (aLong1 - aLong) / 3600;
//        System.out.println(res);
//
//        Long aLong2 = DateUtil.distanceZeroLength(bigInteger);
//        System.out.println(aLong2);
//        timeStamp.subtract(timeStamp.subtract(timeStamp.divideAndRemainder(Constant.DAYSTAMPTIME))

    }
}
