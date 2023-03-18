package cn.kankancloud.switchcenter;

import cn.kankancloud.jbp.web.domain.EnableDomainInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.kankancloud.switchcenter.adapter.driving.repository.*.dao")
@EnableDomainInjector(basePackages = {"cn.kankancloud.switchcenter.adapter.domain"})
public class ApplicationStarter {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ApplicationStarter.class)
                .registerShutdownHook(true)
                .run(args);
    }
}
