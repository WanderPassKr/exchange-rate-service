package com.wanderpass.exchange_rate_service.exchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "openex")
public class OpenExchangeProperties {
    private String apiKey;
    private String baseUrl;
}
