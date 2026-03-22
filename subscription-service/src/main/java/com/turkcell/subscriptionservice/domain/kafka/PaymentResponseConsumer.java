package com.turkcell.subscriptionservice.domain.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.event.PaymentResponseEvent;
import com.turkcell.subscriptionservice.application.subscription.SubscriptionStrategy;
import com.turkcell.subscriptionservice.application.subscription.util.SubscriptionStrategyFactory;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionStatus;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import com.turkcell.subscriptionservice.infrastructure.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseConsumer {

    private final SubscriptionRepository repository;
    private final ObjectMapper objectMapper;
    private final NotificationProducer notificationProducer;
    @KafkaListener(
            topics = "payment-response",
            groupId = "sub-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consumePaymentResponse(String message) {
        PaymentResponseEvent event;
        try {
            event = objectMapper.readValue(message, PaymentResponseEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Payment response parse error : {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        Subscription sub = repository.findByCorrelationId(UUID.fromString(event.getCorrelationId()))
                .orElseThrow(() -> new EntityNotFoundException("subscription.not.found"));

        if (sub.getStatus() != SubscriptionStatus.PENDING_PAYMENT) {
            log.warn("This message already processed: {}", event.getCorrelationId());
            return;
        }

        if (event.isSuccess()) {
            sub.activate(1);
        } else {
            sub.fail(event.getErrorMessage());
        }
        repository.save(sub);
        notificationProducer.buildNotificationEvent(event);
    }
}