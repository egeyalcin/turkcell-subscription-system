package com.turkcell.event;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseEvent extends BaseEvent {

    private boolean success;
    private String subscriptionId;
    private BigDecimal amount;
    private String currency;
    private String errorCode;
    private String errorMessage;
    private LocalDateTime processedAt;
    private String transactionReference;
}