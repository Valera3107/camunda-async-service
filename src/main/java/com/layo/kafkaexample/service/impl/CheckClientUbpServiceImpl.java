package com.layo.kafkaexample.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.layo.kafkaexample.client.VklClient;
import com.layo.kafkaexample.config.AsynchronousServiceTask;
import com.layo.kafkaexample.data.dto.checkClientUbp.RequestVklDto;
import com.layo.kafkaexample.service.AsyncService;
import com.layo.kafkaexample.service.common.MessageQueue;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckClientUbpServiceImpl implements AsyncService {

    public static final String RESULT = "result";

    private final VklClient vklClient;

    private final ObjectMapper mapper;

    @Override
    public void invoke(MessageQueue.Message message, ProcessEngine processEngine) throws JsonProcessingException {

        Map<String, Object> data = message.getData();
        var executionId = (String) data.get(AsynchronousServiceTask.EXECUTION_ID);

        var request = mapper.readValue((String) data.get("request"), RequestVklDto.class);

        var response = vklClient.checkClient(request);
        response.setRequestId(request.getId());
        Map<String, Object> callback = Collections.singletonMap(RESULT, mapper.writeValueAsString(response));
        processEngine.getRuntimeService().signal(executionId, callback);
    }
}
