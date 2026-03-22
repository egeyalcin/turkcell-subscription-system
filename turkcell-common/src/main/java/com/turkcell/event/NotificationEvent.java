package com.turkcell.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent extends BaseEvent {
    private Long userId;
    private String userEmail;
    private String userPhoneNumber;
    private boolean success;
    private String subscriptionId;
    private NotificationType eventType;
    private Map<String, String> metadata;
}