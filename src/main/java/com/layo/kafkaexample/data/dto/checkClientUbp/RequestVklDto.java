package com.layo.kafkaexample.data.dto.checkClientUbp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO запроса к интеграционому сервису VKL
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder(toBuilder = true)
public class RequestVklDto {

    /**
     * Конструктор с default значениеями
     * @param id - Id запроса
     * @param dpCode - Номер ДП
     */
    public RequestVklDto(String id, String dpCode) {
        this.id = id;
        this.dpCode = dpCode;
        this.product = "P113";
        this.call = "CALL_0";
        this.inet = 4;
    }

    /**
     * ID запроса
     */
    @JsonProperty("ID")
    private String id;

    /**
     * Номер ДП
     * Обязательный параметр
     */
    @JsonProperty("dpCode")
    private String dpCode;

    /**
     * Продукт
     * Обязательный параметр
     * По умолчанию - P113
     */
    @JsonProperty("product")
    private String product;

    /**
     * Номер вызова
     * Обязательный параметр
     * По умолчанию - CALL_0
     */
    @JsonProperty("call")
    private String call;

    /**
     * Номер методики
     * Обязательный параметр
     * По умолчанию - 4
     */
    @JsonProperty("inet")
    private Integer inet;

}
