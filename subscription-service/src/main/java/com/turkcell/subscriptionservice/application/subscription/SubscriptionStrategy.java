package com.turkcell.subscriptionservice.application.subscription;

import com.turkcell.subscriptionservice.domain.constants.SubscriptionType;
import com.turkcell.subscriptionservice.domain.entity.Subscription;

public interface SubscriptionStrategy {

    SubscriptionType getSubscriptionType();

    Subscription createPendingSubscription(Long userId, Long planId);

}