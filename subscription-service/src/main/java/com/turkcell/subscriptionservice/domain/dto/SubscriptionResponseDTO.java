package com.turkcell.subscriptionservice.domain.dto;

import com.turkcell.subscriptionservice.domain.constants.SubscriptionStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {

    private UUID subscriptionId;

    private String correlationId;

    private SubscriptionStatus status;

    private String message;

    private LocalDateTime createdAt;
}