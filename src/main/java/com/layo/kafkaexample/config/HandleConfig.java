package com.layo.kafkaexample.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("ubrr.distancecredit.config")
public class HandleConfig {

    /**
     * URL к api dbms service
     */
    private String dbmsRestUrl;

    /**
     * URL к api dbms service
     */
    @Value("${ubrr.distancecredit.kafka-topics.checkuserubp.input-topic}")
    private String checkUserUbpInputTopic;

    /**
     * URL к api dbms service
     */
    @Value("${ubrr.distancecredit.kafka-topics.checkuserubp.output-topic}")
    private String checkUserUbpOutputTopic;
}
