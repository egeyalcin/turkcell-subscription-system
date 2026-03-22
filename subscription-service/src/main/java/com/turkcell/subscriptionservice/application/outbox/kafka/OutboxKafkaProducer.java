package com.turkcell.subscriptionservice.application.outbox.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.event.SubscriptionCreatedEvent;
import com.turkcell.subscriptionservice.domain.entity.OutboxMessage;
import com.turkcell.subscriptionservice.domain.exception.SubscriptionProcessException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @CircuitBreaker(name = "kafkaOutboxBreaker", fallbackMethod = "kafkaFallback")
    public void sendToKafkaWithCircuitBreaker(OutboxMessage message) throws Exception {
        log.info("Sending message to Kafka. ID: {}, Topic: {}", message.getId(), message.getTopic());
        
        SubscriptionCreatedEvent event = objectMapper.readValue(message.getPayload(), SubscriptionCreatedEvent.class);
        
        kafkaTemplate.send(message.getTopic(), event.getCorrelationId(), message.getPayload())
                .get(3, TimeUnit.SECONDS);
    }

    public void kafkaFallback(OutboxMessage message, Throwable t) {
        log.warn("Circuit Breaker is OPEN or Kafka is Down! Message ID: {}. Error: {}",
                 message.getId(), t.getMessage());
        throw new SubscriptionProcessException("kafka.unavailable");
    }
}