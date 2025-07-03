package com.wanderpass.exchange_rate_service.global.exception.handler;

import com.wanderpass.exchange_rate_service.global.exception.type.BusinessException;
import com.wanderpass.exchange_rate_service.global.exception.type.IntegrationException;

import com.wanderpass.exchange_rate_service.global.exception.type.StatusCode;
import com.wanderpass.exchange_rate_service.global.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getStatusCode().getStatus())
                .body(ErrorResponse.of(ex.getStatusCode()));
    }

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrationException(IntegrationException ex) {
        return ResponseEntity
                .status(ex.getStatusCode().getStatus())
                .body(ErrorResponse.of(ex.getStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandledException(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(StatusCode.INTERNAL_ERROR, "예기치 못한 오류가 발생했습니다."));
    }
}
