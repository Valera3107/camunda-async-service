package com.layo.kafkaexample.service.common;

import com.layo.kafkaexample.client.DbmsClient;
import com.layo.kafkaexample.config.HandleConfig;
import com.layo.kafkaexample.data.dto.checkClientUbp.RequestVklDto;
import com.layo.kafkaexample.engine.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final Logger logger = LoggerFactory.getLogger(SendMessageService.class);

    private final Producer<RequestVklDto> requestVklProducer;

    private final DbmsClient client;

    private final HandleConfig config;

    public void checkUserByUbp(String processId) {
        try {
            var applicant = client.getApplicantByRequestId(processId);

            ListenableFuture<SendResult<String, RequestVklDto>> listenableFuture = this.requestVklProducer.sendMessage(config.getCheckUserUbpInputTopic(), "IN_KEY", new RequestVklDto(processId, applicant.getDp()));

            SendResult<String, RequestVklDto> result = listenableFuture.get();
            logger.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition(), result.getRecordMetadata().serializedValueSize()));
        } catch (ExecutionException | InterruptedException ex) {
            logger.info("Cause error during execution: " + ex.getCause());
        }
    }
}