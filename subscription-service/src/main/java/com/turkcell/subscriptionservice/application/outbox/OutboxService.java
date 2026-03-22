package com.turkcell.subscriptionservice.application.outbox;

import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.entity.Subscription;

public interface OutboxService {
    void saveSubscriptionCreatedEvent(Subscription subscription, SubscriptionCreateRequest request);
}
