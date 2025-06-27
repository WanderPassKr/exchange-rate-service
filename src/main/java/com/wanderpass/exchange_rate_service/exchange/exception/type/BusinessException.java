package com.wanderpass.exchange_rate_service.exchange.exception.type;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final StatusCode statusCode;

    public BusinessException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}