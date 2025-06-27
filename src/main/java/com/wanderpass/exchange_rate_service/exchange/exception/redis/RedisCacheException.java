package com.wanderpass.exchange_rate_service.exchange.exception.redis;

import com.wanderpass.exchange_rate_service.exchange.exception.type.IntegrationException;
import com.wanderpass.exchange_rate_service.exchange.exception.type.StatusCode;

public class RedisCacheException extends IntegrationException {
    public RedisCacheException(String message) {
        super(StatusCode.REDIS_CACHE_FAILURE, message);
    }
}
