package com.tj.bigdata.analysis.util;

public class MainTest {
    public static void main(String[] args) {
        //获取当前时间戳  精确到小时
        Long nowTimeStampHour = DateUtil.getNowTimeStampHour();
        System.out.println(nowTimeStampHour);
    }
}
