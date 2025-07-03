package com.wanderpass.exchange_rate_service.global.exception.type;

import lombok.Getter;

@Getter
public class IntegrationException extends RuntimeException {

    private final StatusCode statusCode;

    public IntegrationException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
