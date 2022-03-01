package com.tj.bigdata.analysis.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

/**
 * <h1>时间开始结束处理</h1>
 *
 * @author tianji
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class TimerHandler {


    /**
     * 记录开始时间以及结束时间,用于计算差值
     */
    public static class TimeDate {
        final StringBuffer sb;
        @Getter
        final Date startTime;
        @Getter
        final Date endTime;
        final Supplier<String> intervalTimeStringLazy;

        static class FormatData extends Date {
            static FieldPosition fieldPosition;
            private final StringBuffer sb;

            static {
                try {
                    Field field = Class.forName("java.text.DontCareFieldPosition").getDeclaredField("INSTANCE");
                    field.setAccessible(true);
                    fieldPosition = (FieldPosition) field.get(null);
                } catch (Exception e) {
                    log.error("无法通过映射获取 java.text.DontCareFieldPosition.INSTANCE");
                }
            }

            FormatData() {
                sb = new StringBuffer(28);
            }

            @Override
            public String toString() {
                if (fieldPosition != null) {
                    sb.setLength(0);
                    return dateFormat.format(this, sb, fieldPosition).toString();
                }
                return dateFormat.format(this);
            }
        }

        public TimeDate() {
            sb = new StringBuffer(512);
            startTime = new FormatData();
            endTime = new FormatData();
            intervalTimeStringLazy = new Supplier<String>() {
                @Override
                public String get() {
                    return intervalTimeString();
                }

                @Override
                public String toString() {
                    return get();
                }
            };
        }

        Supplier<String> intervalTimeStringLazy() {
            return intervalTimeStringLazy;
        }


        String intervalTimeString() {
            long timeDifference = processTime();
            long numberOfMilliseconds = timeDifference % 1000L;
            long numberOfSeconds = timeDifference / 1000L;
            long numberOfMinutes = numberOfSeconds / 60L;
            long numberOfHours = numberOfMinutes / 60L;
            long numberOfDays = numberOfHours / 24L;

            numberOfHours %= 24;
            numberOfMinutes %= 60;
            numberOfSeconds %= 60;

            sb.setLength(0);
            sb.append(numberOfDays).append("天 ");
            sb.append(numberOfHours).append("时 ");
            sb.append(numberOfMinutes).append("分 ");
            sb.append(numberOfSeconds).append("秒 ");
            sb.append(numberOfMilliseconds).append("毫秒");
            return sb.toString();
        }

        long intervalTime() {
            return endTime.getTime() - startTime.getTime();
        }
    }

    private static final ThreadLocal<TimeDate> copyOnThreadLocal;
    private static final SimpleDateFormat dateFormat;

    static {
        copyOnThreadLocal = ThreadLocal.withInitial(TimeDate::new);
//        dateFormat        = new SimpleDateFormat(DateUtil.DEFAULT_DATE_REGEX);
        dateFormat = new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_RFGFX);
        dateFormat.setLenient(false);
    }

    public TimerHandler() {

    }

    public static void start() {
        copyOnThreadLocal.get().startTime.setTime(System.currentTimeMillis());
    }

    public static void end() {
        copyOnThreadLocal.get().endTime.setTime(System.currentTimeMillis());
    }

    public static void end(Logger logger) {
        end();
        log(logger);
    }

    public static void log(Logger logger) {
        TimeDate timerDate = copyOnThreadLocal.get();
        logger.info("开始时间：{} 结束时间：{} 耗时：{} ", timerDate.startTime, timerDate.endTime, timerDate.intervalTimeStringLazy());
    }

    /**
     * 时间差（毫秒）
     *
     * @return 时间差
     */
    public static Long processTime() {
        return copyOnThreadLocal.get().intervalTime();
    }


    /**
     * 获取当前线程 TimeDate 对象
     *
     * @return TimeDate
     */
    public static TimeDate get() {
        return copyOnThreadLocal.get();
    }
}

