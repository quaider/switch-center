package com.cdfsunrise.switchcenter;

import com.cdfsunrise.smart.framework.web.domain.EnableDomainInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cdfsunrise.switchcenter.adapter.driving.repository.*.dao")
// @Import(SwaggerConfiguration.class)
@EnableDomainInjector(basePackages = {"com.cdfsunrise.switchcenter.adapter.domain"})
public class ApplicationStarter {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ApplicationStarter.class)
                .registerShutdownHook(true)
                .run(args);
    }
}
