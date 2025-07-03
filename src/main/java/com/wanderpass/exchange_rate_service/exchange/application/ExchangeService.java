package com.wanderpass.exchange_rate_service.exchange.application;

import com.wanderpass.exchange_rate_service.exchange.domain.entity.ExchangeRate;
import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import com.wanderpass.exchange_rate_service.exchange.infrastructure.persistence.ExchangeRateRepository;
import com.wanderpass.exchange_rate_service.exchange.infrastructure.redis.ExchangeRateCacheRepository;
import com.wanderpass.exchange_rate_service.exchange.infrastructure.redis.ExchangeRateData;
import com.wanderpass.exchange_rate_service.exchange.presentation.dto.request.ConvertRequest;
import com.wanderpass.exchange_rate_service.exchange.presentation.dto.response.ConvertResponse;
import com.wanderpass.exchange_rate_service.global.exception.type.BusinessException;
import com.wanderpass.exchange_rate_service.global.exception.type.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRateCacheRepository exchangeRateCacheRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public ConvertResponse convert(ConvertRequest request) {
        Currency from = request.fromCurrency();
        Currency to = request.toCurrency();

        ExchangeRateData cached = exchangeRateCacheRepository.get(from, to);

        // fallback 처리를 위해서 null 반환을 처리해줘야한다.
        if (cached == null) {
            ExchangeRate rate = exchangeRateRepository
                    .findTopByFromCurrencyAndToCurrencyOrderByFetchedAtDesc(from, to)
                    .orElseThrow(() ->
                            new BusinessException(StatusCode.NOT_FOUND, "레디스에 미존재하는 데이터를 MySQL에서 조회했지만 존재하지않는 환율 데이터입니다.")
                    );
            exchangeRateCacheRepository.save(from, to, rate.getRate(), rate.getFetchedAt());
            cached = ExchangeRateData.builder()
                    .rate(rate.getRate())
                    .fetchedAt(rate.getFetchedAt())
                    .build();
        }

        BigDecimal convertedAmount = BigDecimal.valueOf(request.amount())
                .multiply(cached.getRate())
                .setScale(2, RoundingMode.HALF_UP);

        return ConvertResponse.builder()
                .requestId(request.requestId())
                .fromCurrency(request.fromCurrency().name())
                .toCurrency(request.toCurrency().name())
                .rate(cached.getRate())
                .convertedAmount(convertedAmount)
                .fetchedAt(cached.getFetchedAt())
                .build();
    }
}
