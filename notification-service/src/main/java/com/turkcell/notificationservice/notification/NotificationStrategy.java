package com.turkcell.notificationservice.notification;

import com.turkcell.event.NotificationEvent;
import com.turkcell.event.NotificationType;

public interface NotificationStrategy {

    NotificationType getSupportedType();

    void sendNotification(NotificationEvent event);
}
