package com.layo.kafkaexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties
public class KafkaExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaExampleApplication.class, args);
    }
}
