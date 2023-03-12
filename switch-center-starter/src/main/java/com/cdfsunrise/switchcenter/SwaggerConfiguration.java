package com.cdfsunrise.switchcenter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConditionalOnProperty(value = "knife4j.enable", havingValue = "true")
public class SwaggerConfiguration {

    @Bean(value = "createRestApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("switch-center接口文档")
                        .description("switch-center接口文档")
                        .contact(new Contact("研发7组", null, null))
                        .version("1.0")
                        .build())
                .groupName("后台管理类") // 分组名称
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cdfsunrise.switchcenter.adapter.api")) // controller 扫描路径
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "createRestOpenApi")
    public Docket createRestOpenApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("switch-center openApi接口文档")
                        .description("switch-center openApi接口文档")
                        .contact(new Contact("研发7组", null, null))
                        .version("1.0")
                        .build())
                .groupName("开放平台类") // 分组名称
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cdfsunrise.switchcenter.adapter.openapi")) // controller 扫描路径
                .paths(PathSelectors.any())
                .build();
    }
}
