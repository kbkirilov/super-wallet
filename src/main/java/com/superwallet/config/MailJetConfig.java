package com.superwallet.config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailJetConfig {

    @Value("d8db532ed3ba3253b1f12a314c250ae2")
    private String apiKey;

    @Value("${MAILJET_API_SECRET}")
    private String apiSecret;

    @Bean
    public MailjetClient mailjetClient() {
        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecret)
                .build();
        return new MailjetClient(options);
    }

}
