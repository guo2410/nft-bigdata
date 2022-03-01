package com.tj.bigdata.analysis.MyAnnotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author guoch
 */
@Slf4j
@JsonSerialize
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal != null){
            BigDecimal ethRes = bigDecimal.movePointLeft(18).setScale(6, BigDecimal.ROUND_HALF_UP);
            jsonGenerator.writeNumber(ethRes);
        }else {
            jsonGenerator.writeNumber(new BigDecimal(0));
        }
    }
}
