package com.turkcell.subscriptionservice.application.subscription.strategies;

import com.turkcell.subscriptionservice.application.subscription.SubscriptionStrategy;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionStatus;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionType;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import com.turkcell.subscriptionservice.infrastructure.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GncSubscriptionStrategy implements SubscriptionStrategy {

    private final SubscriptionRepository repository;

    @Override
    public SubscriptionType getSubscriptionType() {
        return SubscriptionType.GNC;
    }

    @Override
    public Subscription createPendingSubscription(Long userId, Long planId) {
        Subscription subscription = Subscription.builder()
                .userId(userId)
                .planId(planId)
                .type(SubscriptionType.GNC)
                .status(SubscriptionStatus.PENDING_PAYMENT)
                .build();

        return repository.saveAndFlush(subscription);
    }

}