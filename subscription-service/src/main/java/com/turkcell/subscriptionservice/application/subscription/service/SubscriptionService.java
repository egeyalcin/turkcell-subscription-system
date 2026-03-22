package com.turkcell.subscriptionservice.application.subscription.service;

import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionResponseDTO;

import java.util.UUID;

public interface SubscriptionService {

    SubscriptionResponseDTO createSubscription(SubscriptionCreateRequest request);

    void cancelSubscription(UUID id);

    SubscriptionResponseDTO getSubscription(UUID id);
}