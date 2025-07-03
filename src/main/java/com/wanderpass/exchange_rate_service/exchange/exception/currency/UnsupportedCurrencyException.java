package com.wanderpass.exchange_rate_service.exchange.exception.currency;

import com.wanderpass.exchange_rate_service.global.exception.type.BusinessException;
import com.wanderpass.exchange_rate_service.global.exception.type.StatusCode;

public class UnsupportedCurrencyException extends BusinessException {
    public UnsupportedCurrencyException(String currencyCode) {
        super(StatusCode.INVALID_INPUT, "지원하지 않는 통화 코드입니다: " + currencyCode);
    }
}
