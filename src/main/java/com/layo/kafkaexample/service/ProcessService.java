package com.layo.kafkaexample.service;

import org.springframework.scheduling.annotation.Async;

/**
 * Создание процесса для запуска асинхронной задачи
 */
public interface ProcessService<T> {

    @Async
    void runProcess(T request, String processName, String outputTopic);
}
