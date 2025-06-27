package com.wanderpass.exchange_rate_service.exchange.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanderpass.exchange_rate_service.exchange.application.dto.ExchangeRateResponse;
import com.wanderpass.exchange_rate_service.exchange.domain.entity.ExchangeRate;
import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import com.wanderpass.exchange_rate_service.exchange.exception.exchange.ExchangeRateResponseEmptyException;
import com.wanderpass.exchange_rate_service.exchange.exception.redis.RedisCacheException;
import com.wanderpass.exchange_rate_service.exchange.infrastructure.client.ExchangeRateClient;
import com.wanderpass.exchange_rate_service.exchange.infrastructure.persistence.ExchangeRateBatchRepository;
import com.wanderpass.exchange_rate_service.exchange.infrastructure.persistence.ExchangeRateRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateSyncService {

    private final ExchangeRateClient client;
    private final ExchangeRateCalculator calculator;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateBatchRepository exchangeRateBatchRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 동기 방식으로 OpenAPI 호출 → 이후 로직은 트랜잭션이 보장되는 범위 내에서 처리
     */
    public void syncAll() {
        ExchangeRateResponse response = client.getLatestRatesWithRetry().block(); // 동기 변환
        if (response == null) {
            throw new ExchangeRateResponseEmptyException("환율 API 응답이 null입니다.");
        }
        syncAllInternal(response);
    }

    @Transactional
    public void syncAllInternal(ExchangeRateResponse response) {
        Map<Currency, BigDecimal> usdRates = response.toCurrencyRateMap();
        LocalDateTime now = LocalDateTime.now();

        Map<Currency, Map<Currency, BigDecimal>> allRates = calculator.calculateAllPairs(usdRates);
        List<ExchangeRate> entities = new ArrayList<>();

        allRates.forEach((from, toMap) -> {
            toMap.forEach((to, rate) -> {
                entities.add(ExchangeRate.of(from, to, rate, now));
                cacheToRedis(from, to, rate, now);
            });
        });

        exchangeRateBatchRepository.batchInsert(entities);
    }

    private void cacheToRedis(Currency from, Currency to, BigDecimal rate, LocalDateTime fetchedAt) {
        try {
            String key = "exchange_rate:" + from + ":" + to;
            String value = objectMapper.writeValueAsString(
                    Map.of("rate", rate, "fetchedAt", fetchedAt.toString())
            );
            redisTemplate.opsForValue().set(key, value, Duration.ofHours(1));
        } catch (JsonProcessingException e) {
            throw new RedisCacheException("Redis 캐싱 실패");
        }
    }
}
