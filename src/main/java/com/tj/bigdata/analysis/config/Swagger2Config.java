//package com.tj.bigdata.analysis.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//
//@Configuration
//@EnableSwagger2
//public class Swagger2Config {
//    @Bean
//    public Docket webApiConfig(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("webApi")
//                .apiInfo(webApiInfo())
//                .select()
////                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
////                .paths(Predicates.not(PathSelectors.regex("/error.*")))
//                .build();
//    }
//    @Bean
//    public Docket adminApiConfig(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("adminApi")
//                .apiInfo(adminApiInfo())
//                .select()
////                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
//                .build();
//    }
//    private ApiInfo webApiInfo(){
//        return new ApiInfoBuilder()
//                .title("API文档")
//                .description("服务接口定义")
//                .version("1.0")
//                .contact(new Contact("tianji", "", ""))
//                .build();
//    }
//    private ApiInfo adminApiInfo(){
//        return new ApiInfoBuilder()
//                .title("心API文档")
//                .description("服务接口定义")
//                .version("1.0")
//                .contact(new Contact("tianji", "", "m"))
//                .build();
//    }
//}
