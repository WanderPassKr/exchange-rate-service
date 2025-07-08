package com.wanderpass.exchange_rate_service.exchange.presentation.controller;

import com.wanderpass.exchange_rate_service.exchange.application.ExchangeService;
import com.wanderpass.exchange_rate_service.exchange.presentation.dto.request.ConvertRequest;
import com.wanderpass.exchange_rate_service.exchange.presentation.dto.response.ConvertResponse;
import com.wanderpass.exchange_rate_service.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeService exchangeService;

    @PostMapping("/convert")
    public ResponseEntity<ApiResponse<ConvertResponse>> convert(@RequestBody ConvertRequest request) {
        ConvertResponse response = exchangeService.convert(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
