package com.wanderpass.exchange_rate_service.exchange.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertResponse {
    private String requestId;
    private BigDecimal convertedAmount;
    private BigDecimal rate;
    private String fromCurrency;
    private String toCurrency;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fetchedAt;
}


