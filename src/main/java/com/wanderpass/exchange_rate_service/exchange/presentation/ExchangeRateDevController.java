package com.wanderpass.exchange_rate_service.exchange.presentation;

import com.wanderpass.exchange_rate_service.exchange.application.ExchangeRateSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev/exchange-rates") // dev용 prefix
@RequiredArgsConstructor
public class ExchangeRateDevController {

    private final ExchangeRateSyncService syncService;

    @PostMapping("/sync")
    public String syncExchangeRatesManually() {
        syncService.syncAll(); // block → 내부에서 syncAllInternal() 호출
        return "환율 동기화가 완료되었습니다.";
    }
}
