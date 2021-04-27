package com.layo.kafkaexample.engine.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.layo.kafkaexample.client.DbmsClient;
import com.layo.kafkaexample.config.HandleConfig;
import com.layo.kafkaexample.data.dto.checkClientUbp.RequestDbmsDto;
import com.layo.kafkaexample.data.dto.common.ResponseDto;
import com.layo.kafkaexample.data.dto.common.ResultDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(value = "example.kafka.consumer-enabled", havingValue = "true")
@RequiredArgsConstructor
public class HandleVklResponseConsumer {

    private final Logger logger = LoggerFactory.getLogger(HandleVklResponseConsumer.class);

    private final ObjectMapper mapper;

    private final DbmsClient dbmsClient;

    private final HandleConfig config;

    private static final String DICTIONARY_URL_PATH = "/dictionaryValues/";

    private static final String NEW_STATUS_NAME_APPROVED = "УБП. Одобрение";

    private static final String NEW_STATUS_NAME_REFUSED = "УБП. Отказ";

    private static final Long STATUS_CODE_APPROVED = 2L;

    private static final Long SUCCESS_STATUS = 1L;

    private static final Long ERROR_RESULT_CODE = 99L;

    @KafkaListener(topics = {"OUTPUT_DATA"})
    public void consume(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final String response,
                        final Acknowledgment acknowledgment
    ) throws JsonProcessingException {

        var dto = mapper.readValue(response, ResponseDto.class);

        logger.info(String.format("#### -> Consumed message -> response: %s\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", dto, message, offset, key, partition, topic));

        var resultCodes = dto.getResult().stream().map(ResultDto::getResult).collect(Collectors.toList());

        if (SUCCESS_STATUS.equals(dto.getStatus()) && !resultCodes.contains(ERROR_RESULT_CODE)) {
            changeStatus(dto, resultCodes);
        } else {
            logger.info("Operation has completed with code: " + ERROR_RESULT_CODE);
        }

        acknowledgment.acknowledge();
    }

    private void changeStatus(ResponseDto dto, List<Long> resultCodes) {
        var statusName = resultCodes.contains(STATUS_CODE_APPROVED) ? NEW_STATUS_NAME_APPROVED : NEW_STATUS_NAME_REFUSED;
        var status = dbmsClient.getStatusByValue(statusName);
        dbmsClient.updateRequest(dto.getRequestId(), new RequestDbmsDto(config.getDbmsRestUrl().concat(DICTIONARY_URL_PATH).concat(status.getId())));
    }
}
