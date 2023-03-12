package com.cdfsunrise.switchcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Indexed;

@Indexed
@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cdfsunrise.switchcenter.adapter.driving.repository.*.dao")
@Import(SwaggerConfiguration.class)
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class);
    }
}
