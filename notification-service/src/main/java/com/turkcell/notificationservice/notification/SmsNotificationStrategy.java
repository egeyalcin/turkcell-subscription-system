package com.turkcell.notificationservice.notification;

import com.turkcell.event.NotificationEvent;
import com.turkcell.event.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public NotificationType getSupportedType() {
        return NotificationType.SMS;
    }

    @Override
    public void sendNotification(NotificationEvent event) {
        log.info("[SMS SENT] To: {}, Message: Your subscription {} is {}!",
                event.getUserPhoneNumber(), event.getSubscriptionId(), event.isSuccess());
    }
}