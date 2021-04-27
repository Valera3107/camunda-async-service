package com.layo.kafkaexample.controller;

import com.layo.kafkaexample.data.dto.checkClientUbp.RequestDto;
import com.layo.kafkaexample.service.common.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

    private final SendMessageService service;

    @PostMapping("/checkUserUbp/publish")
    public void checkUserUbp(@RequestBody RequestDto requestDto) {
        service.checkUserByUbp(requestDto.getProcessId());
    }
}
