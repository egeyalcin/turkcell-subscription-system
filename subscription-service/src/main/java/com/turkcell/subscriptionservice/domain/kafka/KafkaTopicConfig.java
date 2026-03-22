package com.turkcell.subscriptionservice.domain.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic subscriptionCreatedTopic() {
        return TopicBuilder.name("subscription-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentResponseTopic() {
        return TopicBuilder.name("payment-response")
                .partitions(3)
                .build();
    }
}