package com.tj.bigdata.analysis.exception;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.JsonParseException;
import com.tj.bigdata.analysis.constant.Constant;
import com.tj.bigdata.analysis.util.ResponseData;
import com.tj.bigdata.analysis.util.ResultUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static com.tj.bigdata.analysis.util.ResponseData.*;


/**
 * 全局异常捕获
 *
 * @author guoch
 */
@ControllerAdvice(annotations = {RestController.class, Component.class})
public class ExceptionAdvice {

    private final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> restClientExceptionHandle(RestClientException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007,
                "rest-client-interface-error", Constant.MESSAGE_NETWORK_UNSTABLE);
    }


    @ResponseBody
    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> resourceAccessExceptionHandle(ResourceAccessException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007,
                "network-error", Constant.MESSAGE_NETWORK_UNSTABLE);
    }

    /**
     * json处理失败
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(JSONException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> jsonExceptionHandle(JSONException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007, "json-handle-error", e.getMessage());
    }

    /**
     * Json参数格式不正确
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> jsonParseExceptionHandle(JsonParseException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007,
                "json-parameter-format-error", e.getMessage());
    }

    /**
     * URL参数格式不正确
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> numberFormatExceptionHandle(MethodArgumentTypeMismatchException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007,
                "url-parameter-format-error", e.getMessage());
    }

    /**
     * 请求方法不正确
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> httpMediaTypeNotSupportedExceptionHandle(
            HttpMediaTypeNotSupportedException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007,
                "request-method-error", e.getMessage());
    }

    /**
     * HTTP消息不可读
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> httpMessageNotReadableExceptionHandle(
            HttpMessageNotReadableException e) {
        logger.error(ExceptionUtils.getMessage(e));
        return ResultUtils.getResult(ResultUtils.CODE_40007, ResultUtils.MESSAGE_40007,
                "http-message-not-read-error", e.getMessage());
    }

    /**
     * 业务处理异常
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> businessExceptionHandle(BusinessException e) {
        return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + e.getSubCode(), e.getSubMessage());
    }


    /**
     * 业务处理异常
     *
     * @param e the e
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> bizExceptionHandle(BizException e) {
        logger.error(e.getMessage());
        return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + e.getSubCode(), e.getSubMessage());
    }

    /**
     * 服务器内部错误
     *
     * @param e 参数
     * @return the map
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> commonExceptionHandle(Exception e) {
        logger.error("server-inside-error-{}", e.getMessage());
        if (BusinessException.class.equals(e.getClass())) {
            BusinessException biz = BusinessException.class.cast(e);
            return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + biz.getSubCode(), biz.getSubMessage());
        }

        if (BizException.class.equals(e.getClass())) {
            BizException biz = BizException.class.cast(e);
            return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + biz.getSubCode(), biz.getMessage());
        }

        if (BizException.class.equals(e.getClass())) {
            BizException biz = BizException.class.cast(e);
            return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + biz.getSubCode(), biz.getSubMessage());
        }

        if (BizException.class.equals(e.getClass())) {
            BizException biz = BizException.class.cast(e);
            return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + biz.getSubCode(), biz.getSubMessage());
        }

        if (BizException.class.equals(e.getClass())) {
            BizException biz = BizException.class.cast(e);
            return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + biz.getSubCode(), biz.getSubMessage());
        }
        return ResponseData.getResult(CODE_40007, MESSAGE_40007, PLATFORM + "network-error", "网络异常，请稍后重试");
    }
}
