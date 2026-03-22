package com.turkcell.subscriptionservice.domain.dto;

import com.turkcell.subscriptionservice.domain.constants.SubscriptionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Plan ID cannot be null")
    private Long planId;

    @NotNull(message = "Subscription type must be selected")
    private SubscriptionType type;

    @NotBlank(message = "Card holder name cannot be blank")
    private String cardHolderName;

    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;

    @NotBlank(message = "Expiry month cannot be blank")
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Invalid month format (01-12)")
    private String expiryMonth;

    @NotBlank(message = "Expiry year cannot be blank")
    @Pattern(regexp = "^[0-9]{2}$", message = "Year format must be YY (e.g., 26)")
    private String expiryYear;

    @NotBlank(message = "CVV cannot be blank")
    @Size(min = 3, max = 3, message = "CVV must be 3 digits")
    private String cvv;
}