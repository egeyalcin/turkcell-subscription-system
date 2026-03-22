package com.turkcell.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class SubscriptionCreatedEvent extends BaseEvent {

    private UUID subscriptionId;
    private Long userId;
    private Long planId;
    private String cardHolderName;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private BigDecimal price;
    private String idempotencyKey;
}