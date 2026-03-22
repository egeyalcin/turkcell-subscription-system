package com.turkcell.notificationservice.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.event.NotificationEvent;
import com.turkcell.notificationservice.notification.util.NotificationProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationProviderService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notification", groupId = "notification-service-group")
    public void consume(String event) {
        try {
            NotificationEvent notificationEvent = objectMapper.readValue(event, NotificationEvent.class);
            notificationService.processNotification(notificationEvent);
        } catch (JsonProcessingException e) {
            log.info("Error occurred : {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}