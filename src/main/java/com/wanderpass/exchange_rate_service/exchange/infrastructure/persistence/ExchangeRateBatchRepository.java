package com.wanderpass.exchange_rate_service.exchange.infrastructure.persistence;

import com.wanderpass.exchange_rate_service.exchange.domain.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExchangeRateBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void batchInsert(List<ExchangeRate> exchangeRates) {
        String sql = "INSERT INTO exchange_rate (from_currency, to_currency, rate, fetched_at) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                exchangeRates,
                1000, // 배치 크기
                (ps, exchangeRate) -> {
                    ps.setString(1, exchangeRate.fromCurrency.name());
                    ps.setString(2, exchangeRate.toCurrency.name());
                    ps.setBigDecimal(3, exchangeRate.rate);
                    ps.setTimestamp(4, Timestamp.valueOf(exchangeRate.fetchedAt));
                }
        );
    }
}
