package com.tj.bigdata.analysis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *线程池参数
 *
 * @author guoch
 */
@ConfigurationProperties(prefix = "tianji.thread")
@Data
@Component
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
