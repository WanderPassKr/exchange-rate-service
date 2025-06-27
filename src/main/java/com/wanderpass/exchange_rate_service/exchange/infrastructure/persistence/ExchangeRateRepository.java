package com.wanderpass.exchange_rate_service.exchange.infrastructure.persistence;

import com.wanderpass.exchange_rate_service.exchange.domain.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {}
