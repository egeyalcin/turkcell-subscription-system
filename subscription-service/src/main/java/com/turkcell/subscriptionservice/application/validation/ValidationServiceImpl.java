package com.turkcell.subscriptionservice.application.validation;

import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImpl implements ValidationService {


    @Override
    public void validate(SubscriptionCreateRequest request) {

    }
}
