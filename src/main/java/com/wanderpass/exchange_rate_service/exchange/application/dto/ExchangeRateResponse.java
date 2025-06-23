package com.wanderpass.exchange_rate_service.exchange.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

    private String disclaimer;
    private String license;
    private Long timestamp;
    private String base;

    @JsonProperty("rates")
    private Map<String, BigDecimal> rates = new HashMap<>();
}
