package com.turkcell.subscriptionservice.domain.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.event.BaseEvent;
import com.turkcell.event.NotificationEvent;
import com.turkcell.event.NotificationType;
import com.turkcell.event.PaymentResponseEvent;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import com.turkcell.subscriptionservice.infrastructure.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final SubscriptionRepository subscriptionRepository;

    private final ObjectMapper objectMapper;

    public void buildNotificationEvent(PaymentResponseEvent event) {
        Optional<Subscription> subscription =
                subscriptionRepository.findByCorrelationId(UUID.fromString(event.getCorrelationId()));
        if (subscription.isPresent()) {
            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .eventType(NotificationType.ALL)
                    .subscriptionId(event.getSubscriptionId())
                    .success(event.isSuccess())
                    .userEmail("mockMail")
                    .userId(subscription.get().getUserId())
                    .userPhoneNumber("05541111111")
                    .correlationId(event.getCorrelationId())
                    .createdAt(LocalDateTime.now())
                    .eventId(UUID.randomUUID().toString())
                    .build();
            sendKafkaNotification(notificationEvent);
        }

    }

    private void sendKafkaNotification(NotificationEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("notification", event.getCorrelationId(), eventJson);
        } catch (JsonProcessingException e) {
            log.error("Error occurred! : {}", e.getLocalizedMessage());
        }
    }
}
