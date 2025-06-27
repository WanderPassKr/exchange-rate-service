package com.wanderpass.exchange_rate_service.exchange.application;

import com.wanderpass.exchange_rate_service.exchange.domain.type.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExchangeRateCalculator {

    public Map<Currency, Map<Currency, BigDecimal>> calculateAllPairs(Map<Currency, BigDecimal> usdRates) {
        Map<Currency, Map<Currency, BigDecimal>> allRates = new HashMap<>();

        for (Currency from : usdRates.keySet()) {
            Map<Currency, BigDecimal> toMap = new HashMap<>();
            for (Currency to : usdRates.keySet()) {
                if (!from.equals(to)) {
                    BigDecimal rate = usdRates.get(to).divide(usdRates.get(from), 10, RoundingMode.HALF_UP);
                    toMap.put(to, rate);
                }
            }
            allRates.put(from, toMap);
        }

        return allRates;
    }
}
