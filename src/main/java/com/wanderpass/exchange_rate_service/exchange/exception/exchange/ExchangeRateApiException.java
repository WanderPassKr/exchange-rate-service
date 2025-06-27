package com.wanderpass.exchange_rate_service.exchange.exception.exchange;

import com.wanderpass.exchange_rate_service.exchange.exception.type.IntegrationException;
import com.wanderpass.exchange_rate_service.exchange.exception.type.StatusCode;

public class ExchangeRateApiException extends IntegrationException {
    public ExchangeRateApiException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
