package com.layo.kafkaexample.engine.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.layo.kafkaexample.config.HandleConfig;
import com.layo.kafkaexample.data.dto.checkClientUbp.RequestVklDto;
import com.layo.kafkaexample.service.ProcessService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "example.kafka.consumer-enabled", havingValue = "true")
@RequiredArgsConstructor
public class RequestVklConsumer {

    private final Logger logger = LoggerFactory.getLogger(RequestVklConsumer.class);

    private final ObjectMapper mapper;

    private final HandleConfig config;

    @Qualifier(value = "checkClientUbpProcessServiceImpl")
    private final ProcessService<RequestVklDto> processService;

    @KafkaListener(topics = {"INPUT_DATA"})
    public void consume(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final String request,
                        final Acknowledgment acknowledgment
    ) throws JsonProcessingException {
        var value = mapper.readValue(request, RequestVklDto.class);
        logger.info(String.format("#### -> Consumed message -> request: %s\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", value, message, offset, key, partition, topic));
        acknowledgment.acknowledge();

        processService.runProcess(value, "asynchronousVklInvocation", config.getCheckUserUbpOutputTopic());
    }
}

