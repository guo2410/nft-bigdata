package com.tj.bigdata.analysis.config;

import com.tj.bigdata.analysis.constant.KafkaConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guoch
 */
@Configuration
public class KafkaInitialConfiguration {

    @Bean
    public NewTopic blockTopic(){
        return new NewTopic(KafkaConstant.FLINK_ES_TX,1,(short) 1);
    }

}
