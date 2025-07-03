package com.wanderpass.exchange_rate_service.global.dto;


import com.wanderpass.exchange_rate_service.global.exception.type.StatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(StatusCode.OK.getCode())
                .message(message)
                .data(data)
                .build();
    }
}
