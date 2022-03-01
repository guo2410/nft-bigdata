package com.tj.bigdata.analysis.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * <h1>对象判空</h1>
 *
 * @author tianji
 * @version 1.0
 * @since JDK 1.8
 */
public class ObjUtil {

    private ObjUtil() {

    }

    /**
     * 判断对象是否为空
     */
    public static String isNull(Object obj, String... methodNameArr) {
        StringBuilder sb = new StringBuilder();
        Class c = obj.getClass();
        Field[] sfieldList = c.getDeclaredFields();
        Field[] pFieldList = c.getSuperclass().getDeclaredFields();
        Field[] fieldList = new Field[sfieldList.length + pFieldList.length];
        System.arraycopy(sfieldList, 0, fieldList, 0, sfieldList.length);
        System.arraycopy(pFieldList, 0, fieldList, sfieldList.length, pFieldList.length);
        for (int i = 0; i < methodNameArr.length; i++) {
            String methodName = methodNameArr[i];
            boolean _t = isNull(obj, fieldList, methodName);
            if (_t) {
                sb.append(methodName);
                sb.append(",");
            }
        }
        String retStr = sb.toString().trim();
        if (!retStr.equals("")) {
            return retStr.substring(0, retStr.length() - 1);
        }
        return null;
    }

    private static boolean isNull(Object obj, Field[] fieldList, String methodName) {
        try {
            for (int i = 0; i < fieldList.length; i++) {
                Field field = fieldList[i];
                field.setAccessible(true);
                if (field.getName().equals(methodName)) {
                    if (field.getGenericType().toString().indexOf("String") != -1) {
                        if (field.get(obj) == null) {
                            return true;
                        }
                        return isNull(field.get(obj).toString());
                    } else {
                        return isNull(field.get(obj));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static boolean isNull(String obj) {
        if (obj == null || obj.trim().equals("")) {
            return true;
        }
        return false;
    }

    private static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 获取错误信息
     *
     * @param e 错误
     * @return 错误信息
     */
    public static String getMessage(Throwable e) {
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
