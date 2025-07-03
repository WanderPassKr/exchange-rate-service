package com.wanderpass.exchange_rate_service.exchange.infrastructure.redis;

import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import com.wanderpass.exchange_rate_service.exchange.exception.redis.RedisCacheException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateCacheRepository {

    private final RedisTemplate<String, ExchangeRateData> redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofHours(1);

    public ExchangeRateData get(Currency from, Currency to) {
        String key = createKey(from, to);
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis 환율 조회 실패: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    public void save(Currency from, Currency to, BigDecimal rate, LocalDateTime fetchedAt) {
        String key = createKey(from, to);
        ExchangeRateData data = ExchangeRateData.builder()
                .rate(rate)
                .fetchedAt(fetchedAt)
                .build();

        try {
            redisTemplate.opsForValue().set(key, data, CACHE_TTL);
        } catch (Exception e) {
            throw new RedisCacheException("Redis 캐싱 실패" + e);
        }
    }

    private String createKey(Currency from, Currency to) {
        return String.format("exchange_rate:%s:%s", from, to);
    }
}
