package com.wanderpass.exchange_rate_service.exchange.domain.entity;

import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency", nullable = false)
    public Currency fromCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency", nullable = false)
    public Currency toCurrency;

    @Column(name = "rate", nullable = false, precision = 38, scale = 10)
    public BigDecimal rate;

    @Column(name = "fetched_at", nullable = false, updatable = false)
    @CreationTimestamp
    public LocalDateTime fetchedAt;

    private ExchangeRate(Currency fromCurrency, Currency toCurrency, BigDecimal rate, LocalDateTime fetchedAt) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.fetchedAt = fetchedAt;
    }

    public static ExchangeRate of(Currency fromCurrency, Currency toCurrency, BigDecimal rate, LocalDateTime fetchedAt) {
        return new ExchangeRate(fromCurrency, toCurrency, rate, fetchedAt);
    }
}
