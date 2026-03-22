package com.turkcell.subscriptionservice.application.mapper;

import com.turkcell.event.SubscriptionCreatedEvent;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionResponseDTO;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "id", target = "subscriptionId")
    SubscriptionResponseDTO mapToResponse(Subscription subscription);

}
