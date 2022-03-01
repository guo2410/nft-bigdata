package com.tj.bigdata.analysis.util;

import java.lang.reflect.Field;

public class ReflexHandler {
    public ReflexHandler() {
    }

    public static <P, F> F get(P pojo, String field, Class<F> type) {
        Object result = null;

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("get");
            sb.append(field.substring(0, 1).toUpperCase());
            sb.append(field.substring(1));
            result = type.cast(pojo.getClass().getMethod(sb.toString()).invoke(pojo));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (F) result;
    }

    public static <P, F> void set(P pojo, String field, F value) {
        try {
            Field fieldTemp = pojo.getClass().getDeclaredField(field);
            Class<?>[] parameterTypes = new Class[]{fieldTemp.getType()};
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(field.substring(0, 1).toUpperCase());
            sb.append(field.substring(1));
            pojo.getClass().getMethod(sb.toString(), parameterTypes).invoke(pojo, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
