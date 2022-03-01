package com.tj.bigdata.analysis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;

import com.tj.bigdata.analysis.exception.BizException;
import org.springframework.util.Assert;

public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String JSON_CONVERT_ERROR = "json-convert-error";

    private JsonUtils() {
    }

    public static String toJson(Object value) {
        Assert.notNull(value, "value 值不能为空");

        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException var2) {
            throw new BizException("json-convert-error", "对象转换成json值异常");
        }
    }

    public static <T> T toObject(String json, Class<T> valueType) {
        Assert.hasText(json, "json 参数不能为空");
        Assert.notNull(valueType, "Class<T> 转换类型不能为空");

        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException var3) {
            throw new BizException("json-convert-error", "Class<T> 转换对象异常");
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        Assert.hasText(json, "json 参数不能为空");
        Assert.notNull(typeReference, "typeReference 转换类型不能为空");

        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException var3) {
            throw new BizException("json-convert-error", "typeReference 转换对象异常");
        }
    }

    public static <T> T toObject(String json, JavaType javaType) {
        Assert.hasText(json, "json 参数不能为空");
        Assert.notNull(javaType, "typeReference 转换类型不能为空");

        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException var3) {
            throw new BizException("json-convert-error", "javaType 转换对象异常");
        }
    }

    public static JsonNode toTree(String json) {
        Assert.hasText(json, "json 参数不能为空");

        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException var2) {
            throw new BizException("json-convert-error", "JsonNode 转换对象异常");
        }
    }

    public static void writeValue(Writer writer, Object value) {
        Assert.notNull(writer, "JSON流 不能为空");
        Assert.notNull(value, "value 不能为空");

        try {
            OBJECT_MAPPER.writeValue(writer, value);
        } catch (IOException var3) {
            throw new BizException("json-convert-error", "对象转换为JSON流（Writer）");
        }
    }

    public static JavaType constructType(Type type) {
        Assert.notNull(type, "type 类型不能为空");
        return TypeFactory.defaultInstance().constructType(type);
    }

    public static JavaType constructType(TypeReference<?> typeReference) {
        Assert.notNull(typeReference, "typeReference 类型不能为空");
        return TypeFactory.defaultInstance().constructType(typeReference);
    }
}

