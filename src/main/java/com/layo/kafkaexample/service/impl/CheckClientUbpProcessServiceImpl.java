package com.layo.kafkaexample.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.layo.kafkaexample.data.dto.checkClientUbp.RequestVklDto;
import com.layo.kafkaexample.data.dto.common.ResponseDto;
import com.layo.kafkaexample.engine.producer.Producer;
import com.layo.kafkaexample.service.AsyncService;
import com.layo.kafkaexample.service.ProcessService;
import com.layo.kafkaexample.service.common.MessageQueue;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CheckClientUbpProcessServiceImpl implements ProcessService<RequestVklDto> {

    private final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    public static final String RESULT = "result";

    private final ProcessEngine processEngine;

    private final MessageQueue queue;

    private final AsyncService asyncService;

    private final ObjectMapper mapper;

    private final Producer<ResponseDto> producer;

    @Async
    @Override
    public void runProcess(RequestVklDto request, String processName, String outputTopic) {

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("request", mapper.writeValueAsString(request));

            var runtimeService = processEngine.getRuntimeService();
            var taskService = processEngine.getTaskService();
            var processInstance = runtimeService.startProcessInstanceByKey(processName, variables);

            Task waitStateBefore = taskService.createTaskQuery()
                    .taskDefinitionKey("waitStateBefore")
                    .processInstanceId(processInstance.getId())
                    .singleResult();

            taskService.complete(waitStateBefore.getId());

            MessageQueue.Message resultMessage = queue.getNextMessage();

            asyncService.invoke(resultMessage, processEngine);

            variables = runtimeService.getVariables(processInstance.getId());

            sendResultToConsumer(variables, outputTopic);
        } catch (JsonProcessingException | InterruptedException | ExecutionException ex) {
            logger.info("An error has occurred during camunda process execution: " + ex.getMessage());
        }
    }

    public void sendResultToConsumer(Map<String, Object> variables, String topic) throws JsonProcessingException, InterruptedException, ExecutionException {

        var value = mapper.readValue((String) variables.get(RESULT), ResponseDto.class);

        ListenableFuture<SendResult<String, ResponseDto>> listenableFuture = this.producer.sendMessage(topic, "OUT_KEY", value);

        SendResult<String, ResponseDto> result = listenableFuture.get();

        logger.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", result.getRecordMetadata().topic(),
                result.getRecordMetadata().offset(),
                result.getRecordMetadata().partition(), result.getRecordMetadata().serializedValueSize()));
    }
}
