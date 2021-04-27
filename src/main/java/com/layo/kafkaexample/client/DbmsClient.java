package com.layo.kafkaexample.client;

import com.layo.kafkaexample.config.FeignConfiguration;
import com.layo.kafkaexample.data.dto.checkClientUbp.ApplicantDto;
import com.layo.kafkaexample.data.dto.checkClientUbp.RequestDbmsDto;
import com.layo.kafkaexample.data.dto.common.StatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "DbmsServiceClient", url = "${ubrr.distancecredit.config.dbms_rest_url}", configuration = FeignConfiguration.class)
public interface DbmsClient {

    @GetMapping("/requests/{id}/applicant")
    ApplicantDto getApplicantByRequestId(@PathVariable String id);

    @GetMapping("/dictionaryValues/search/byValue")
    StatusDto getStatusByValue(@RequestParam("value") String value);

    @PatchMapping("/requests/{id}")
    void updateRequest(@PathVariable("id") String id, @RequestBody RequestDbmsDto requestData);
}
