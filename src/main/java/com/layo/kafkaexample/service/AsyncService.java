package com.layo.kafkaexample.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.layo.kafkaexample.service.common.MessageQueue;
import org.camunda.bpm.engine.ProcessEngine;

/**
 * Создание бизнес логики в ходе запуска асинхронной задачи
 */
public interface AsyncService {

    void invoke(MessageQueue.Message message, ProcessEngine processEngine) throws JsonProcessingException;
}
