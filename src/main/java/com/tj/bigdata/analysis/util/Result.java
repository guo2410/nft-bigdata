package com.tj.bigdata.analysis.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>结果</h1>
 *
 * @author tianji
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@SuppressWarnings({"serial"})
public class Result<D> implements Serializable {

    /**
     * <em>编码属性</em>
     */
    public static final String CODE = "code";
    /**
     * <em>消息属性</em>
     */
    public static final String MESSAGE = "message";
    /**
     * <em>附属编码属性</em>
     */
    private static final String SUB_CODE = "sub_code";
    /**
     * <em>附属消息属性</em>
     */
    private static final String SUB_MESSAGE = "sub_message";
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("附属编码")
    private String sub_code;
    @ApiModelProperty("附属消息")
    private String sub_message;
    @ApiModelProperty("总数")
    @JsonInclude(Include.NON_NULL)
    private Long count;
    @ApiModelProperty("数据")
    private D data;

    private Result() {
        super();
    }

    public static <T, D> Result<D> of(T type) {
        return Result.init(type, null, null);
    }

    public static <T, D> Result<D> of(T type, D data) {
        return Result.init(type, data, null);
    }

    public static <T, D> Result<D> of(T type, D data, Long count) {
        return Result.init(type, data, count);
    }

    /**
     * <i>初始化</i>
     *
     * @param type 类型
     * @param data 数据
     */
    private static <T, D> Result<D> init(T type, D data, Long count) {
        Result<D> result = new Result<>();
        result.setCode(ReflexHandler.get(type, Result.CODE, String.class));
        result.setMessage(ReflexHandler.get(type, Result.MESSAGE, String.class));
        if (StringUtils.isNotBlank(ReflexHandler.get(type, Result.SUB_CODE, String.class))) {
            result.setSub_code(ReflexHandler.get(type, Result.SUB_CODE, String.class));
            result.setSub_message(ReflexHandler.get(type, Result.SUB_MESSAGE, String.class));
        } else if (Objects.nonNull(count)) {
            result.setCount(count);
        } else {
            result.setSub_code(null);
            result.setSub_message(null);
        }
        result.setData(data);
        return result;
    }

    /**
     * <i>getData 方法</i>
     *
     * @param dateType 数据类型
     *                 <ul>
     *                 <li>说明：<u>通过此类型得到相应的值</u>
     *                 <li>语法：<u>T.class（具体使用如下）</u>
     *                 <li>示例：{@code T.class}
     *                 </ul>
     */
    public <T> T getData(Class<T> dateType) {
        return dateType.cast(this.data);
    }

    /**
     * <i>类型</i>
     *
     * <ul>
     * <li>说明：<u>通过此类型设置相应的提示</li>
     * </ul>
     */
    @AllArgsConstructor
    public enum Type {

        /**
         * <em>成功</em>
         */
        SUCCESS_ZH("10000", "成功"),
        /**
         * <em>服务不可用</em>
         */
        SERVICE_UNAVAILABLE_ZH("40000", "服务不可用"),
        /**
         * <em>业务权限不足</em>
         */
        BUSSINESS_PERMISSION_DENIED_ZH("40001", "业务权限不足"),
        /**
         * <em>业务缺少必选参数</em>
         */
        BUSSINESS_MISSSING_PARAMETER_ZH("40002", "业务缺少必选参数"),
        /**
         * <em>业务参数非法</em>
         */
        BUSINESS_ARGS_ILLEGAL_ZH("40003", "业务非法的参数"),
        /**
         * <em>非法的插件</em>
         */
        BUSINESS_PLUGIN_ILLEGAL_ZH("40004", "非法的插件"),
        /**
         * <em>业务上传失败</em>
         */
        BUSINESS_UPLOAD_FAIL_ZH("40005", "业务上传失败"),
        /**
         * <em>业务下载失败</em>
         */
        BUSINESS_DOWNLOAD_FAIL_ZH("40006", "业务下载失败"),
        /**
         * <em>业务处理失败</em>
         */
        BUSINESS_PROCESS_FAIL_ZH("40007", "业务处理失败");

        /**
         * 编码
         */
        @Getter
        private String code;

        /**
         * 消息
         */
        @Getter
        private String message;

        /**
         * 编码
         */
        @Getter
        @Setter
        private String sub_code;

        /**
         * 消息
         */
        @Getter
        @Setter
        private String sub_message;


        private Type(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

    @SuppressWarnings("unused")
    private class Instance {

        private Instance() {
        }
    }

}