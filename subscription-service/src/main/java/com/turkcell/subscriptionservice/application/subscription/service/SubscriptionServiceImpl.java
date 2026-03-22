package com.turkcell.subscriptionservice.application.subscription.service;

import com.turkcell.subscriptionservice.application.mapper.SubscriptionMapper;
import com.turkcell.subscriptionservice.application.outbox.OutboxService;
import com.turkcell.subscriptionservice.application.subscription.SubscriptionStrategy;
import com.turkcell.subscriptionservice.application.subscription.util.SubscriptionStrategyFactory;
import com.turkcell.subscriptionservice.application.validation.ValidationService;
import com.turkcell.subscriptionservice.domain.aspect.Loggable;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionResponseDTO;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import com.turkcell.subscriptionservice.infrastructure.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final OutboxService outboxService;
    private final SubscriptionMapper mapper;
    private final ValidationService validationService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionStrategyFactory factory;

    @Override
    @Transactional
    @Loggable
    public SubscriptionResponseDTO createSubscription(SubscriptionCreateRequest request) {
        validationService.validate(request);
        SubscriptionStrategy subscriptionStrategy = factory.getStrategy(request.getType());
        Subscription subscription = subscriptionStrategy.createPendingSubscription(
                request.getUserId(),
                request.getPlanId()
        );
        outboxService.saveSubscriptionCreatedEvent(subscription, request);
        log.info("Subscription created: {}", subscription.getId());
        return mapper.mapToResponse(subscription);
    }

    @Override
    @Loggable
    public void cancelSubscription(UUID id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);
        subscription.ifPresent(sub -> {
            sub.cancel();
            subscriptionRepository.saveAndFlush(subscription.get());
        });
    }

    @Override
    @Loggable
    public SubscriptionResponseDTO getSubscription(UUID id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);
        if(subscription.isPresent()) {
            return mapper.mapToResponse(subscription.get());
        }
        return SubscriptionResponseDTO.builder()
                .message("Subscription doesn't exist")
                .build();
    }
}