package com.tj.bigdata.analysis.config;


import org.apache.http.HttpHost;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guoch
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${spring.es.host}")
    private String host;

    @Value("${spring.es.port}")
    private int port;

    @Bean
    public RestHighLevelClient esRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        //new HttpHost("103.44.247.5", 28000, "http")).setPathPrefix("elasticsearch").setRequestConfigCallback(builder -> builder.setConnectTimeout(5000 * 1000).setSocketTimeout(6000 * 1000)));
                        new HttpHost(host, port, "http")).setRequestConfigCallback(builder -> builder.setConnectTimeout(50000 * 1000).setSocketTimeout(60000 * 1000)));
        return client;
    }

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory
                .HeapBufferedResponseConsumerFactory(1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

}
