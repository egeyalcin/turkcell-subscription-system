package com.turkcell.subscriptionservice.application.cron;

import com.turkcell.subscriptionservice.application.outbox.OutboxService;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionStatus;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionType;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.entity.Subscription;
import com.turkcell.subscriptionservice.infrastructure.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionCreationJob extends QuartzJobBean {

    private final OutboxService outboxService;
    private final SubscriptionRepository repository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Subscription task started.");
        try {
            List<Subscription> renewals = repository.findSubscriptionsToRenew(SubscriptionStatus.ACTIVE.name());            // Örnek: Süresi dolan abonelikleri bul (UUID bazlı)
            renewals.forEach(sub -> {
                SubscriptionCreateRequest mockSubscriptionCreateRequest = getMockSubscriptionCreateRequest(sub);
                outboxService.saveSubscriptionCreatedEvent(sub, mockSubscriptionCreateRequest);
            });

            log.info("Subscription Job executed successfully.");
        } catch (Exception e) {
            log.error("Error occurred! ", e);
            JobExecutionException qe = new JobExecutionException(e);
            qe.setRefireImmediately(true);
            throw qe;
        }
    }

    private SubscriptionCreateRequest getMockSubscriptionCreateRequest(Subscription sub) {
        return SubscriptionCreateRequest.builder()
                .userId(sub.getUserId())
                .planId(sub.getPlanId())
                .type(SubscriptionType.PLATINUM)
                .cardHolderName("ege yalcin")
                .cardNumber("11111111111")
                .expiryMonth("11")
                .expiryYear("2030")
                .cvv("357")
                .build();
    }
}