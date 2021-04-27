package com.layo.kafkaexample.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Информации о статусе из словаря
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusDto {

    /**
     * Entity Id
     */
    @JsonProperty("id")
    private String id;
}
