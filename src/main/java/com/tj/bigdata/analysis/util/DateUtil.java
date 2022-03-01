package com.tj.bigdata.analysis.util;

import com.google.common.collect.Maps;
import com.tj.bigdata.analysis.constant.Constant;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 日期工具
 *
 * @author gw
 * @version 1.0
 */
public final class DateUtil {

    /**
     * 默认的日期格式 .
     */
    public static final String DEFAULT_REGEX_YYYYMMDD = "yyyyMMdd";

    /**
     * 默认的日期格式 .
     */
    public static final Long HourTimeStamp = 3600L;
    /**
     * 默认的DateFormat 实例
     */
    private static final EPNDateFormat DEFAULT_FORMAT_YYYYMMDD =
            new EPNDateFormat(DEFAULT_REGEX_YYYYMMDD);
    /**
     * 默认的日期格式 .
     */
    public static String DEFAULT_DATE_REGEX = "yyyy-MM-dd";
    /**
     * 默认的DateFormat 实例
     */
    private static final EPNDateFormat DEFAULT_FORMAT = new EPNDateFormat(DEFAULT_DATE_REGEX);
    /**
     * 默认的日期格式 .
     */
    public static String DEFAULT_DATE_TIME_RFGFX = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认的日期格式 .
     */
    public static String DEFAULT_DATE_TIME_SPECIAL = "yyyy-MM-dd HH:00:00";

    /**
     * 日期格式,年和月
     */
    public static String DEFAULT_DATE_REGEX_YYYY_MM = "yyyy-MM";

    /**
     * 格式化当前时间(年/月)
     */
    private static final DateFormat DATE_FORMAT_YEAR_MONTH = new SimpleDateFormat(DEFAULT_DATE_REGEX_YYYY_MM);

    /**
     * 默认的DateFormat 实例
     */
    private static final EPNDateFormat DEFAULT_FORMAT_YYYY_MM_DD_HH_MIN_SS =
            new EPNDateFormat(DEFAULT_DATE_TIME_RFGFX);
    /**
     * 时间戳方式
     */
    public static String DEFAULT_DATE_TIMESTAMP = "yyyyMMddHHmmss";
    private static Map<String, EPNDateFormat> formatMap = Maps.newConcurrentMap();

    static {
        formatMap.put(DEFAULT_DATE_REGEX, DEFAULT_FORMAT);
        formatMap.put(DEFAULT_DATE_TIME_RFGFX, DEFAULT_FORMAT_YYYY_MM_DD_HH_MIN_SS);
        formatMap.put(DEFAULT_REGEX_YYYYMMDD, DEFAULT_FORMAT_YYYYMMDD);
    }

    private DateUtil() {

    }

    public static Long timeStampHour(BigInteger timeStamp) {
        return timeStamp.subtract((timeStamp.mod(Constant.HOURSTAMPTIMEBIGDECIMAL))).longValue();
    }

    public static Long timeStampDay(BigInteger timeStamp) {
        return timeStamp.subtract((timeStamp.mod(Constant.DAYSTAMPTIMEBIGDECIMAL))).longValue();
    }

    /**
     *  获取当前整点时间戳
     *
     * @return 时
     */
    public static Long getCurrHourTime() {
        long now = System.currentTimeMillis() / 1000L;
        long daySecond = 60 * 60;
        return (now - now % daySecond);
    }

    /**
     * 获取当前整日时间戳
     *
     * @return 天
     */
    public static Long getCurrDayTime() {
        long now = System.currentTimeMillis() / 1000L;
        long daySecond = 60 * 60 * 24;
        return now - now % daySecond;
    }


    public static Long distanceZeroLength(BigInteger timeStamp) {
        Long hourTime = timeStampHour(timeStamp);
        Long dayTime = timeStampDay(timeStamp);
        if (dayTime.equals(hourTime)) {
            return 0L;
        } else {
            return ((hourTime - dayTime) / HourTimeStamp);
        }
    }

    public static void setDateFromat(String dateFormat) {
        if (dateFormat.isEmpty()) {
            throw new IllegalArgumentException("dateFormat can not be blank.");
        }
        DEFAULT_DATE_REGEX = dateFormat;
    }

    public static void setTimeFromat(String timeFormat) {
        if (timeFormat.isEmpty()) {
            throw new IllegalArgumentException("timeFormat can not be blank.");
        }
        DEFAULT_DATE_TIME_RFGFX = timeFormat;
    }

    /**
     * 根据日期获取年月
     */
    public static String getYearMonthByDate(Date date) {
        return DATE_FORMAT_YEAR_MONTH.format(date);
    }

    /**
     * 获取半年前的月份返回时间戳
     */
    public static Date setMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -5);
        return c.getTime();
    }

    public static Date toDate(String dateStr) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_RFGFX);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String toStr(Date date) {
        return toStr(date, DEFAULT_DATE_REGEX);
    }

    public static String toStr(Date date, String format) {
        return getDateFormat(format).format(date);
    }

    /**
     * 时间对象格式化成String ,等同于java.text.DateFormat.format();
     *
     * @param date 需要格式化的时间对象
     *             <p/>
     *             2014年5月5日 下午12:00:00 flyfox 330627517@qq.com
     * @return 转化结果
     */
    public static String format(Date date) {
        return DEFAULT_FORMAT.format(date);
    }

    public static Date format(String date, String regex) {
        SimpleDateFormat sdf = new SimpleDateFormat(regex);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getDate();
    }

    /**
     * Unix时间戳相互转换字符串时间
     *
     * @param timestampString Unix时间戳转换成字符串时间
     */
    public static String timeStampToDate(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date =
                new SimpleDateFormat(DEFAULT_DATE_REGEX).format(new Date(timestamp));
        return date;
    }

    /**
     * Unix时间戳相互转换字符串时间
     *
     * @param timestampString Unix时间戳转换成字符串时间
     */
    public static String timeStampToDate(String timestampString, String rexgex) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date =
                new SimpleDateFormat(rexgex).format(new Date(timestamp));
        return date;
    }

    public static String[] getMonthsByCurrentDate(Date date, Integer monthsNum) {
        String[] monthsArr = new String[monthsNum];
        for (int i = 0; i < monthsNum; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, -i);
            monthsArr[i] = DATE_FORMAT_YEAR_MONTH.format(getMonthDate(c.getTime()).get("start"));
        }
        return monthsArr;
    }

    /**
     * Unix时间戳相互转换字符串时间
     *
     * @param timestamp Unix时间戳转换成字符串时间
     */
    public static String timeStampToDateFormat(long timestamp, String rexgex) {
        return new SimpleDateFormat(rexgex).format(new Date(timestamp));
    }

    public static Integer dateToTimeStamp() {
        return Math.toIntExact(System.currentTimeMillis() / 1000);
    }

    public static Integer dateToIntTimeStamp(Date date) {
        return Math.toIntExact(getUnixTimeStamp(date));
    }

    public static Long dateToLongTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间的时间戳SSS
     */
    public static long getDateByTime() {
        return System.currentTimeMillis();
    }


    public static long getLastTimeStampByInterval() {
        return getDateByTime() - 3456083;
    }

    /**
     * Unix时间戳
     *
     * @param date 时间
     */
    public static Long getUnixTimeStamp(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 时间对象格式化成String ,等同于java.text.SimpleDateFormat.format();
     *
     * @param date  需要格式化的时间对象
     * @param regex 定义格式的字符串
     *              <p/>
     *              2014年5月5日 下午12:00:00 flyfox 330627517@qq.com
     * @return 转化结果
     */
    public static String format(Date date, String regex) {
        return getDateFormat(regex).format(date);
    }

    private static EPNDateFormat getDateFormat(String regex) {
        EPNDateFormat fmt = formatMap.get(regex);
        if (fmt == null) {
            fmt = new EPNDateFormat(regex);
            formatMap.put(regex, fmt);
        }
        return fmt;
    }

    /**
     * 主要是给milano使用，数据库只认java.sql.*
     */
    public static Timestamp getSqlTimestamp(Date date) {
        if (null == date) {
            date = new Date();
        }
        return getSqlTimestamp(date.getTime());
    }

    /**
     * 主要是给milano使用，数据库只认java.sql.*
     */
    public static Timestamp getSqlTimestamp(long time) {
        return new Timestamp(time);
    }

    /**
     * 尝试解析时间字符串 ,if failed return null;
     *
     * @param time 2014年5月5日 下午12:00:00
     */
    public static Date parseByAll(String time) {
        Date stamp = null;
        if (time == null || "".equals(time)) {
            return null;
        }
        Pattern p3 = Pattern.compile("\\b\\d{2}[.-]\\d{1,2}([.-]\\d{1,2}){0,1}\\b");
        if (p3.matcher(time).matches()) {
            time = (time.charAt(0) == '1' || time.charAt(0) == '0' ? "20" : "19") + time;
        }

        stamp = parse(time, "yyyy-MM-ddHH:mm:ss");
        if (stamp == null) {
            stamp = parse(time, "yyyy-MM-dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy.MM.dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy-MM");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy.MM");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy-MM-dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yy-MM-dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy.MM.dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy-MM.dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy.MM-dd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyyMMdd");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy年MM月dd日");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyyMM");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy年MM月");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy");
        }
        if (stamp == null) {
            stamp = parse(time, "yyyy年");
        }
        return stamp;
    }

    /**
     * 解析字符串成时间 ,遇到错误返回null不抛异常
     *
     * @param source 2014年5月5日 下午12:00:00 flyfox 330627517@qq.com
     * @return 解析结果
     */
    public static Date parse(String source) {
        try {
            return DEFAULT_FORMAT.parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析字符串成时间 ,遇到错误返回null不抛异常
     *
     * @param regex 2014年5月5日 下午12:00:00 flyfox 330627517@qq.com
     * @return 解析结果
     */
    public static Date parse(String source, String regex) {
        try {
            EPNDateFormat fmt = getDateFormat(regex);
            return fmt.parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 取得当前时间的Date对象 ;
     * <p/>
     * 2014年5月5日 下午12:00:00 flyfox 330627517@qq.com
     */
    public static Date getNowDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 时间戳转日期
     */
    public static Date transForDate(Integer ms) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_RFGFX);
        Date temp = null;
        try {
            String str = sdf.format(msl);
            temp = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static Date getDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * mongo 日期查询isodate
     */
    public static Date dateToISODate(String dateStr) {
        //T代表后面跟着时间，Z代表UTC统一时间
        Date date = formatD(dateStr);
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        String isoDate = format.format(date);
        try {
            return format.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date formatD(String dateStr) {
        return format(dateStr, DEFAULT_DATE_TIME_RFGFX);
    }

    /**
     * 根据时间差获取天数
     */
    public static long getDaysByTimeDiff(Date date) {
        // 毫秒
        long end = System.currentTimeMillis();
        // 毫秒
        long start = date.getTime();
        return (end - start) / (24 * 3600 * 1000);
    }

    /**
     * 获取当前时间字符串
     */
    public static String getNow() {
        return getNow(DEFAULT_DATE_TIME_SPECIAL);
    }

    public static Long getNowTimeStampHour() {
        return DateFormatUtil.transForMilliSecond(DateUtil.getNow(), DateUtil.DEFAULT_DATE_TIME_SPECIAL);
    }

    /**
     * 获取当前日期字符串
     */
    public static String today() {
        return toStr(new Date());
    }

    public static Long transForMilliSecond(String dateStr) {
        Date date = DateFormatUtil.formatDate(dateStr);
        return date == null ? null : DateFormatUtil.transForMilliSecond(date);
    }


    /**
     * 获取年和月
     *
     * @param date 日期
     * @return 年月字符串
     */
    public static String toYearMonthStr(Date date) {
        return toStr(date, DEFAULT_DATE_REGEX_YYYY_MM);
    }

    /**
     * 获取当前时间字符串
     * <p/>
     * 2014年7月4日 下午11:47:10 flyfox 330627517@qq.com
     *
     * @param regex 格式表达式
     */
    public static String getNow(String regex) {
        return format(getNowDate(), regex);
    }

    /**
     * 两个日期的时间差，返回"X天X小时X分X秒"
     */
    public static String getBetween(Date begin, Date end) {
        // 除以1000是为了转换成秒
        long between = (end.getTime() - begin.getTime()) / 1000;
        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60 / 60;

        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append("天");
        sb.append(hour);
        sb.append("小时");
        sb.append(minute);
        sb.append("分");
        sb.append(second);
        sb.append("秒");

        return sb.toString();
    }

    /**
     * 返回两个日期之间隔了多少小时
     */
    public static int getDateHourSpace(Date start, Date end) {
        int hour = (int) ((start.getTime() - end.getTime()) / 3600 / 1000);
        return hour;
    }

    /**
     * 返回两个日期之间隔了多少天
     */
    public static int getDateDaySpace(Date start, Date end) {
        int day = getDateHourSpace(start, end) / 24;
        return day;
    }

    /**
     * 得到某一天是星期几
     *
     * @param date 日期字符串
     * @return String 星期几
     */
    @SuppressWarnings("static-access")
    public static String getDateInWeek(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        if (dayIndex < 0) {
            dayIndex = 0;
        }
        return weekDays[dayIndex];
    }

    /**
     * 日期减去多少个小时
     *
     * @param hourCount 多少个小时
     */
    public static Date getDateReduceHour(Date date, long hourCount) {
        long time = date.getTime() - 3600 * 1000 * hourCount;
        Date dateTemp = new Date();
        dateTemp.setTime(time);
        return dateTemp;
    }

    /**
     * 日期区间分割
     */
    public static List<Date> getDateSplit(Date start, Date end, long splitCount) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        long between = endTime - startTime;

        long count = splitCount - 1L;
        long section = between / count;

        List<Date> list = new ArrayList<Date>();
        list.add(start);

        for (long i = 1L; i < count; i++) {
            long time = startTime + section * i;
            Date date = new Date();
            date.setTime(time);
            list.add(date);
        }

        list.add(end);

        return list;
    }

    /**
     * 返回两个日期之间隔了多少天，包含开始、结束时间
     */
    public static List<String> getDaySpaceDate(Date start, Date end) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        List<String> dateList = new LinkedList<String>();

        long dayCount =
                (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
        if (dayCount < 0) {
            return dateList;
        }

        dateList.add(format(fromCalendar.getTime(), DEFAULT_DATE_REGEX));

        for (int i = 0; i < dayCount; i++) {
            // 增加一天
            fromCalendar.add(Calendar.DATE, 1);
            dateList.add(format(fromCalendar.getTime(), DEFAULT_DATE_REGEX));
        }

        return dateList;
    }

    /**
     * 获取一天的起始时间
     *
     * @param start 当前时间
     * @param end   (明天1，今天0，昨天-1)
     * @return 开始时间
     */
    public static Date getDateByDay(Date start, int end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DATE, end);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取一天的结束时间
     *
     * @param start 当前时间
     * @param end   (明天1，今天0，昨天-1)
     * @return 结束时间
     */
    public static Date endDateByDay(Date start, int end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DATE, end);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取开始时间
     */
    public static Date startDateByHour(Date start, int end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.MINUTE, end);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取结束时间
     */
    public static Date endDateByHour(Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 根据年份和周得到周的开始和结束日期
     */
    public static Map<String, Date> getStartEndDateByWeek(int year, int week) {
        Calendar weekCalendar = new GregorianCalendar();
        weekCalendar.set(Calendar.YEAR, year);
        weekCalendar.set(Calendar.WEEK_OF_YEAR, week);
        weekCalendar.set(Calendar.DAY_OF_WEEK, weekCalendar.getFirstDayOfWeek());

        // 得到周的开始日期
        Date startDate = weekCalendar.getTime();

        weekCalendar.roll(Calendar.DAY_OF_WEEK, 6);
        // 得到周的结束日期
        Date endDate = weekCalendar.getTime();

        // 开始日期往前推一天
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        // 明天1，昨天-1
        startCalendar.add(Calendar.DATE, 1);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        startDate = startCalendar.getTime();

        // 结束日期往前推一天
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        // 明天1，昨天-1
        endCalendar.add(Calendar.DATE, 1);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);
        endDate = endCalendar.getTime();

        Map<String, Date> map = Maps.newConcurrentMap();
        map.put("start", startDate);
        map.put("end", endDate);
        return map;
    }

    /**
     * 根据日期月份，获取月份的开始时间
     */
    public static Date getMonthStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 得到前一个月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 根据日期月份，获取月份的开始和结束日期
     */
    public static Map<String, Date> getMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 得到前一个月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date start = calendar.getTime();

        // 得到前一个月的最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date end = calendar.getTime();

        Map<String, Date> map = Maps.newConcurrentMap();
        map.put("start", start);
        map.put("end", end);
        return map;
    }

    /**
     * 日期增加指定时间
     */
    public static Date add(Date date, Integer year, Integer month, Integer day, Integer hour, Integer minutes, Integer second) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        if (year != null) {
            calendar.add(Calendar.YEAR, year);
        }
        if (month != null) {
            calendar.add(Calendar.MONTH, month);
        }
        if (day != null) {
            calendar.add(Calendar.DAY_OF_MONTH, day);
        }
        if (hour != null) {
            calendar.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minutes != null) {
            calendar.add(Calendar.MINUTE, minutes);
        }
        if (second != null) {
            calendar.add(Calendar.SECOND, second);
        }
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    public static Date set(Date date, Integer month, Integer day, Integer hour, Integer minutes, Integer second) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        if (month != null) {
            calendar.set(Calendar.MONTH, month);
        }
        if (day != null) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        if (hour != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
        if (minutes != null) {
            calendar.set(Calendar.MINUTE, minutes);
        }
        if (second != null) {
            calendar.set(Calendar.SECOND, second);
        }
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期增加指定年数
     */
    public static Date addYears(Date date, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.YEAR, year);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期增加指定天数
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, days);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 增加天数，最后时间
     *
     * @param start 当前时间
     * @return 最后时间
     */
    public static Date addDaysLast(Date start, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, days);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
//    calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 日期减少指定天数
     */
    public static Date subDays(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, -days);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期增加指定月数
     */
    public static Date addMonths(Date date, int months) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.MONTH, months);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期减少指定月数
     */
    public static Date subMonths(Date date, int months) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.MONTH, -months);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期增加指定小时
     */
    public static Date addHours(Date date, int hours) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    public static Date addMinute(Date date, int minutes) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期减少指定小时
     */
    public static Date subHours(Date date, int hours) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.HOUR_OF_DAY, -hours);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }

    /**
     * 比较两个日期大小，可执行对比的 日期rex表达式
     */
    public static int compareDate(Date leftDate, Date rightDate, String rex) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_RFGFX);
        String leftDateStr = sdf.format(leftDate);
        String rightDateStr = sdf.format(rightDate);
        SimpleDateFormat sdfToOut = new SimpleDateFormat(rex);
        try {
            Date dateleft = sdfToOut.parse(leftDateStr);
            Date dateright = sdfToOut.parse(rightDateStr);
            return dateleft.compareTo(dateright);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Date getDateByStr(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 当前时间增加
     *
     * @param caleType {@link Calendar} class constant
     * @param number   增加数量
     */
    public static Long timeCalcAdd(Integer caleType, Integer number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getDateByTime());
        calendar.add(caleType, number);
        return calendar.getTime().getTime();
    }

    /**
     * 指定时间增加
     *
     * @param time     开始时间
     * @param caleType {@link Calendar} class constant
     * @param number   增加数量
     */
    public static Long timeCalcAdd(Long time, Integer caleType, Integer number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(caleType, number);
        return calendar.getTime().getTime();
    }

    /**
     * 判断当前时间距离第二天凌晨的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * 获取某天的开始时间戳
     */
    public static Long getDayStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 获取某天的结束时间戳
     */
    public static Long getDayEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
//    c.set(Calendar.MILLISECOND, 999);
        return c.getTimeInMillis();
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param d 入参
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param d 入参
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取上周的开始时间
     */
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取上周的结束时间
     */
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本周的开始时间
     */
    public static Date getBeginDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     */
    public static Date getEndDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek(date));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 判断是否周一
     *
     * @param currentDate 时间
     */
    public static int weekStatus(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.timeStampToDateFormat(
                DateUtil.getDayStart(DateUtil.parse(String.valueOf(202010), "yyyyMM")), DEFAULT_DATE_TIME_RFGFX));
        System.out.println(DateUtil.timeStampToDateFormat(
                DateUtil.getDayEnd(DateUtil.parse(String.valueOf(202010), "yyyyMM")), DEFAULT_DATE_TIME_RFGFX));
        System.out.println(DateUtil.getDayStart(new Date()));
        System.out.println(format(new Date(), "yyyyMM"));
        System.out.println(addYears(new Date(), 2));
        System.out.println("本周周一0点时间：" + getBeginDayOfWeek(new Date()).toLocaleString());
        System.out.println("本周周日24点时间：" + getEndDayOfWeek(new Date()).toLocaleString());
        Date begin = getBeginDayOfWeek(new Date());
        System.out.println(format(begin, DEFAULT_DATE_TIME_RFGFX));
        System.out.println(weekStatus(new Date()));
        System.out.println(getDateByDay(new Date(), 0));
        int count = 0;
        if (!ObjUtil.isNullOrEmpty(count)) {
            System.out.println("------------------>");
        }
    }

}


class EPNDateFormat {

    private SimpleDateFormat instance;

    EPNDateFormat() {
        instance = new SimpleDateFormat(DateUtil.DEFAULT_DATE_REGEX);
        instance.setLenient(false);
    }

    EPNDateFormat(String regex) {
        instance = new SimpleDateFormat(regex);
        instance.setLenient(false);
    }

    synchronized String format(Date date) {
        if (date == null) {
            return "";
        }
        return instance.format(date);
    }

    synchronized Date parse(String source) throws ParseException {
        return instance.parse(source);
    }
}

