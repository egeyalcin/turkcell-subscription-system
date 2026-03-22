package com.turkcell.notificationservice.notification.util;

import com.turkcell.event.NotificationEvent;
import com.turkcell.event.NotificationType;
import com.turkcell.notificationservice.notification.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProviderService {

    private final List<NotificationStrategy> strategies;

    public void processNotification(NotificationEvent event) {
        log.info("Processing notification of type: {} for user: {}", event.getEventType(), event.getUserId());

        strategies.stream()
                .filter(strategy -> shouldExecute(strategy, event.getEventType()))
                .forEach(strategy -> strategy.sendNotification(event));
    }

    private boolean shouldExecute(NotificationStrategy strategy, NotificationType targetType) {
        if (targetType == NotificationType.ALL) {
            return true;
        }
        return strategy.getSupportedType() == targetType;
    }
}