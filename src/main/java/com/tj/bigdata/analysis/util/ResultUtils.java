package com.tj.bigdata.analysis.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtils {
    public static final String CODE_10000 = "10000";
    public static final String CODE_40000 = "40000";
    public static final String CODE_40001 = "40001";
    public static final String CODE_40002 = "40002";
    public static final String CODE_40003 = "40003";
    public static final String CODE_40004 = "40004";
    public static final String CODE_40005 = "40005";
    public static final String CODE_40006 = "40006";
    public static final String CODE_40007 = "40007";
    public static final String MESSAGE_40000 = "服务不可用";
    public static final String MESSAGE_40001 = "业务权限不足";
    public static final String MESSAGE_40002 = "业务缺少必选参数";
    public static final String MESSAGE_40003 = "业务非法的参数";
    public static final String MESSAGE_40004 = "非法的插件";
    public static final String MESSAGE_40005 = "业务上传失败";
    public static final String MESSAGE_40006 = "业务下载失败";
    public static final String MESSAGE_40007 = "业务处理失败";
    private static final String SUB_CODE_SUCCESS = "Success";
    private static final String SUB_CODE_NO_DATA = "no_data";

    public ResultUtils() {
    }

    public static Map<String, Object> getResult(List<?> list) {
        Map<String, Object> map = new HashMap();
        String subCode = null;
        String code;
        if (ObjUtils.isNullOrEmpty(list)) {
            code = "10000";
            subCode = "no_data";
        } else if (!ObjUtils.isNullOrEmpty(list)) {
            code = "10000";
            subCode = "Success";
        } else {
            code = "40007";
        }

        map.put("code", code);
        map.put("message", "");
        map.put("sub_code", subCode);
        map.put("sub_message", "");
        map.put("data", list);
        return map;
    }

    public static Map<String, Object> getResult(Object obj) {
        Map<String, Object> map = new HashMap();
        String code = null;
        String subCode = null;
        if (ObjUtils.isNullOrEmpty(obj)) {
            code = "10000";
            subCode = "no_data";
        } else if (!ObjUtils.isNullOrEmpty(obj)) {
            code = "10000";
            subCode = "Success";
            Map<String, Object> childMap = new HashMap();
            if (obj instanceof Boolean) {
                childMap.put("flag", obj);
                map.put("data", childMap);
            } else if (obj instanceof Long) {
                childMap.put("count", obj);
                map.put("data", childMap);
            } else if (obj instanceof Integer) {
                childMap.put("count", obj);
                map.put("data", childMap);
            } else {
                map.put("data", obj);
            }
        }

        map.put("code", code);
        map.put("message", "");
        map.put("sub_code", subCode);
        map.put("sub_message", "");
        return map;
    }

    public static Map<String, Object> getResult(String code, String message, String subCode, String subMessage) {
        Map<String, Object> map = new HashMap();
        map.put("code", code);
        map.put("message", message);
        map.put("sub_code", subCode);
        map.put("sub_message", subMessage);
        return map;
    }
}
