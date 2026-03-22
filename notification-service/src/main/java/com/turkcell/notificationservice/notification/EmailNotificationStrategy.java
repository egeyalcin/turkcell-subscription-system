package com.turkcell.notificationservice.notification;

import com.turkcell.event.NotificationEvent;
import com.turkcell.event.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public NotificationType getSupportedType() {
        return NotificationType.EMAIL;
    }

    @Override
    public void sendNotification(NotificationEvent event) {
        log.info("[EMAIL SENT] To: {}, Content: {} for Subscription: {}",
                event.getUserEmail(), event.isSuccess() ,event.getSubscriptionId());
    }
}