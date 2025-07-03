package com.wanderpass.exchange_rate_service.global.exception.dto;

import com.wanderpass.exchange_rate_service.global.exception.type.StatusCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    private ErrorResponse(int status, String code, String message, LocalDateTime timestamp) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse of(StatusCode statusCode) {
        return new ErrorResponse(
                statusCode.getStatus().value(),
                statusCode.getCode(),
                statusCode.getMessage(),
                LocalDateTime.now()
        );
    }

    public static ErrorResponse of(StatusCode statusCode, String customMessage) {
        return new ErrorResponse(
                statusCode.getStatus().value(),
                statusCode.getCode(),
                customMessage,
                LocalDateTime.now()
        );
    }
}
