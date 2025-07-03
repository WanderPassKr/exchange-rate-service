package com.wanderpass.exchange_rate_service.exchange.infrastructure.persistence;

import com.wanderpass.exchange_rate_service.exchange.domain.entity.ExchangeRate;
import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findTopByFromCurrencyAndToCurrencyOrderByFetchedAtDesc(Currency from, Currency to);
}
