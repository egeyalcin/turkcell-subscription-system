package com.turkcell.subscriptionservice.application.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.event.SubscriptionCreatedEvent;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.entity.OutboxMessage;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import com.turkcell.subscriptionservice.infrastructure.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveSubscriptionCreatedEvent(Subscription sub, SubscriptionCreateRequest request) {

        SubscriptionCreatedEvent event = SubscriptionCreatedEvent.builder()
                .subscriptionId(sub.getId())
                .correlationId(sub.getCorrelationId().toString())
                .userId(sub.getUserId())
                .planId(sub.getPlanId())
                .cardHolderName(request.getCardHolderName())
                .cardNumber(request.getCardNumber())
                .expiryMonth(request.getExpiryMonth())
                .expiryYear(request.getExpiryYear())
                .cvv(request.getCvv())
                .idempotencyKey(sub.getCorrelationId().toString())
                .build();

        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("[OutboxServiceImpl] json parse error : {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        OutboxMessage outboxMessage = OutboxMessage.builder()
                .aggregateId(sub.getId().toString())
                .payload(jsonPayload)
                .topic("subscription-events")
                .build();

        outboxRepository.save(outboxMessage);
    }
}