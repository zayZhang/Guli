package com.guli.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// @EnableEurekaClient 下面的注解更为通用
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.guli.edu","com.guli.common"})
public class EduApplication {
    public static void main(String [] args) {
        SpringApplication.run(EduApplication.class,args);
    }
}
