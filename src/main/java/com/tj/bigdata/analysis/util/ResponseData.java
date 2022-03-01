package com.tj.bigdata.analysis.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 *  响应码
 * <h1>返回结果工具类</h1>
 *
 * @author tj
 */
public class ResponseData {

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
    public static final String SUB_CODE_SUCCESS = "Success";
    public static final String SUB_CODE_NO_DATA = "no_data";
    public static final String PLATFORM = "tj_blockchain.";
    public static final String SPLIT_MARK = "#####";

    private ResponseData() {
    }


    public static Map<String, Object> getResult(String code, String message, String subCode,
                                                String subMessage) {
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("code", code);
        map.put("message", message);
        map.put("sub_code", subCode);
        map.put("sub_message", subMessage);
        return map;
    }

    public static Map<String, Object> getError(String subCode, String subMessage) {
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("code", CODE_40007);
        map.put("message", MESSAGE_40007);
        map.put("sub_code", PLATFORM + subCode);
        map.put("sub_message", subMessage);
        return map;
    }
}

