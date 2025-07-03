package com.wanderpass.exchange_rate_service.exchange.exception.exchange;

import com.wanderpass.exchange_rate_service.global.exception.type.StatusCode;

public class ExchangeRateResponseEmptyException extends ExchangeRateApiException {
    public ExchangeRateResponseEmptyException(String message) {
        super(StatusCode.EXCHANGE_RATE_RESPONSE_EMPTY, message);
    }
}
