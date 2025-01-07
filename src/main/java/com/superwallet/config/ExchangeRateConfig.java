package com.superwallet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeRateConfig {

    @Value("${exchangerate.api.key}")
    private String apiKey;

    private static final String baseUrl = "https://v6.exchangerate-api.com/v6";

    @Bean
    public String getBaseUrl() {
        return baseUrl + "/" + apiKey + "/";
    }

    @Bean
    public String getApiKey() {
        return apiKey;
    }
}
