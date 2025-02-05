package com.superwallet.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ApacheKafkaTopicConfig {

    @Bean
    public NewTopic userActivityTopic() {
        return TopicBuilder.name("user-activity").build();
    }
}
