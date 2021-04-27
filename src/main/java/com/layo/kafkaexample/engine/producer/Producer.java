package com.layo.kafkaexample.engine.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@RequiredArgsConstructor
public class Producer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public ListenableFuture<SendResult<String, T>> sendMessage(String topic, String key, T request) {
        return this.kafkaTemplate.send(topic, key, request);
    }
}
