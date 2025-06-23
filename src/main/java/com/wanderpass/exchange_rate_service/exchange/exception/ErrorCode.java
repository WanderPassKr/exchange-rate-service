package com.wanderpass.exchange_rate_service.exchange.exception;

public enum ErrorCode {
    EXTERNAL_API_BAD_REQUEST("외부 API 요청이 잘못되었습니다."),
    EXTERNAL_API_SERVER_ERROR("외부 API 서버 오류입니다."),
    EXTERNAL_API_UNAUTHORIZED("외부 API 키가 유효하지 않습니다."),
    EXTERNAL_API_RATE_LIMITED("외부 API 호출 한도를 초과했습니다."),
    EXTERNAL_API_UNKNOWN("알 수 없는 외부 API 오류입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

