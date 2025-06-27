package com.wanderpass.exchange_rate_service.exchange.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Currency, BigDecimal> toCurrencyRateMap() {
        return rates.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Currency.from(entry.getKey()), // "KRW" → Currency.KRW
                        Map.Entry::getValue
                ));
    }
}
