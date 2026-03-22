package com.turkcell.subscriptionservice.application.validation;

import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;

public interface ValidationService {
    void validate(SubscriptionCreateRequest request);
}
