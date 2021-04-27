package com.layo.kafkaexample.data.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageErrorDto {

    private String code;

    private String textEn;
}
