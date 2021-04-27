package com.layo.kafkaexample.client;

import com.layo.kafkaexample.data.dto.checkClientUbp.RequestVklDto;
import com.layo.kafkaexample.data.dto.common.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "VklServiceClient", url = "${ubrr.distancecredit.config.vkl_rest_url}")
public interface VklClient {

    @PostMapping("/check_client")
    ResponseDto checkClient(RequestVklDto request);
}
