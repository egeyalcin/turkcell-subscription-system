package com.turkcell.subscriptionservice.application.subscription.util;

import com.turkcell.subscriptionservice.application.subscription.SubscriptionStrategy;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionType;
import com.turkcell.subscriptionservice.domain.exception.SubscriptionProcessException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SubscriptionStrategyFactory {

    private final Map<SubscriptionType, SubscriptionStrategy> strategyMap;

    public SubscriptionStrategyFactory(List<SubscriptionStrategy> strategies) {
        Map<SubscriptionType, SubscriptionStrategy> tempMap = new EnumMap<>(SubscriptionType.class);
        strategies.forEach(strategy -> tempMap.put(strategy.getSubscriptionType(), strategy));
        this.strategyMap = Collections.unmodifiableMap(tempMap);
    }

    public SubscriptionStrategy getStrategy(SubscriptionType type) {
        return Optional.ofNullable(strategyMap.get(type))
                .orElseThrow(() -> new SubscriptionProcessException("subscription.type.not.supported"));
    }
}
