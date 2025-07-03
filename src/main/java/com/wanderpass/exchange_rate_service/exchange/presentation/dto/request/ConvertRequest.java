package com.wanderpass.exchange_rate_service.exchange.presentation.dto.request;

import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;

public record ConvertRequest(String requestId,
                             Currency fromCurrency,
                             Currency toCurrency,
                             Integer amount) {}
