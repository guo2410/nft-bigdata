package com.tj.bigdata.analysis;

import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author guoch
 */
@Slf4j
@EnableSwagger2Doc
@ServletComponentScan
@MapperScan(basePackages = {"com.tj.bigdata.analysis.mapper"})
@ComponentScan(basePackages = {"com.tj.bigdata.analysis"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
@EnableAsync
public class AnalysisFactoryApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AnalysisFactoryApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("大数据 - api服务 - 启动完成");
    }
}
