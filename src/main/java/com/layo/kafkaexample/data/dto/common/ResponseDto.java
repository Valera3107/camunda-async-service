package com.layo.kafkaexample.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDto {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("status")
    private Long status;

    @JsonProperty("msgErr")
    private MessageErrorDto msgErr;

    @JsonProperty("result")
    private List<ResultDto> result;

    private String requestId;
}