package com.wanderpass.exchange_rate_service.exchange.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusCode {
    // Success
    OK(HttpStatus.OK, "S200", "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "S201", "리소스가 성공적으로 생성되었습니다."),

    // Client Errors
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C403", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "요청한 리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "C409", "리소스 충돌이 발생했습니다."),
    LOCK_FAILED(HttpStatus.CONFLICT, "C410", "좌석 락 획득에 실패했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C422", "입력값이 유효하지 않습니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "C429", "요청이 너무 많습니다."),

    // Server Errors
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 내부 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "E503", "서비스를 일시적으로 이용할 수 없습니다."),
    EXCHANGE_RATE_RESPONSE_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "E520", "환율 API 응답이 비어 있습니다."),
    REDIS_CACHE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "E521", "Redis 캐싱 처리 중 오류가 발생했습니다."),
    EXTERNAL_API_BAD_REQUEST(HttpStatus.BAD_REQUEST, "E530", "외부 환율 API 요청이 잘못되었습니다."),
    EXTERNAL_API_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E531", "외부 환율 API 인증에 실패했습니다."),
    EXTERNAL_API_RATE_LIMITED(HttpStatus.TOO_MANY_REQUESTS, "E532", "외부 환율 API 호출이 너무 많습니다."),
    EXTERNAL_API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E533", "외부 환율 API 서버 오류가 발생했습니다."),
    EXTERNAL_API_UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "E534", "외부 환율 API에서 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

