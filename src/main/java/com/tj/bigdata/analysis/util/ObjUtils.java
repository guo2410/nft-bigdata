package com.tj.bigdata.analysis.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class ObjUtils {
    public ObjUtils() {
    }

    private static boolean isNull(Object obj, Field[] fieldList, String methodName) {
        try {
            Field[] var3 = fieldList;
            int var4 = fieldList.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                field.setAccessible(true);
                if (field.getName().equals(methodName)) {
                    if (field.getGenericType().toString().contains("String")) {
                        if (field.get(obj) == null) {
                            return true;
                        }

                        return isNull(field.get(obj).toString());
                    }

                    return isNull(field.get(obj));
                }
            }

            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return true;
        }
    }

    private static boolean isNull(String obj) {
        return obj == null || "".equals(obj.trim());
    }

    private static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map)obj).isEmpty();
        } else if (!(obj instanceof Object[])) {
            return false;
        } else {
            Object[] object = (Object[])((Object[])obj);
            if (object.length == 0) {
                return true;
            } else {
                boolean empty = true;
                Object[] var3 = object;
                int var4 = object.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Object ob = var3[var5];
                    if (!isNullOrEmpty(ob)) {
                        empty = false;
                        break;
                    }
                }

                return empty;
            }
        }
    }
}
