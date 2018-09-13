package com.tele2.subscriptions;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCircuitBreaker
@SpringBootApplication
@EnableTransactionManagement(
        mode = AdviceMode.ASPECTJ)
public class Main {

    @Bean
    @Primary
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
