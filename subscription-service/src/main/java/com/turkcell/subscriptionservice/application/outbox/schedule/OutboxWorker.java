package com.turkcell.subscriptionservice.application.outbox.schedule;

import com.turkcell.subscriptionservice.application.outbox.data.OutboxDataManager;
import com.turkcell.subscriptionservice.application.outbox.kafka.OutboxKafkaProducer;
import com.turkcell.subscriptionservice.domain.constants.OutboxStatus;
import com.turkcell.subscriptionservice.domain.entity.OutboxMessage;
import com.turkcell.subscriptionservice.infrastructure.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxWorker {

    private final OutboxRepository outboxRepository;
    private final OutboxDataManager outboxDataManager;
    private final OutboxKafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 2000)
    @SchedulerLock(name = "OutboxWorker_processMessages", lockAtLeastFor = "1s", lockAtMostFor = "10s")
    public void processOutboxMessages() {
        List<OutboxMessage> messages = outboxRepository.findTop500ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW);
        if (messages.isEmpty()) return;

        outboxDataManager.markAsProcessing(messages.stream().map(OutboxMessage::getId).toList());

        List<Long> successIds = new CopyOnWriteArrayList<>();
        List<Long> failedIds = new CopyOnWriteArrayList<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (OutboxMessage message : messages) {
                executor.submit(() -> {
                    try {
                        kafkaProducer.sendToKafkaWithCircuitBreaker(message);
                        successIds.add(message.getId());
                    } catch (Exception e) {
                        log.error("Message didn't send succesfully. id: {}. Reason: {}", message.getId(), e.getMessage());
                        failedIds.add(message.getId());
                    }
                });
            }
        }

        outboxDataManager.updateStatusBulk(successIds, OutboxStatus.PROCESSED);
        outboxDataManager.updateStatusBulk(failedIds, OutboxStatus.FAILED);
    }

    @Scheduled(fixedRate = 60000)
    @SchedulerLock(name = "OutboxWorker_retryFailedMessages", lockAtLeastFor = "10s", lockAtMostFor = "50s")
    public void retryFailedMessages() {
        List<OutboxMessage> failedMessages = outboxRepository
                .findFailedMessagesInLastTwoMinutes();

        if (failedMessages.isEmpty()) {
            log.debug("No failed messages found in the last 2 minutes.");
            return;
        }

        log.info("Retrying {} failed messages found by retry query.", failedMessages.size());

        outboxDataManager.markAsProcessing(failedMessages.stream().map(OutboxMessage::getId).toList());

        List<Long> successIds = new CopyOnWriteArrayList<>();
        List<Long> failedIds = new CopyOnWriteArrayList<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (OutboxMessage message : failedMessages) {
                executor.submit(() -> {
                    try {
                        kafkaProducer.sendToKafkaWithCircuitBreaker(message);
                        successIds.add(message.getId());
                    } catch (Exception e) {
                        log.error("Retry failed for message id: {}. Reason: {}", message.getId(), e.getMessage());
                        failedIds.add(message.getId());
                    }
                });
            }
        }

        if (!successIds.isEmpty()) outboxDataManager.updateStatusBulk(successIds, OutboxStatus.PROCESSED);
        if (!failedIds.isEmpty()) outboxDataManager.updateStatusBulk(failedIds, OutboxStatus.FAILED);
    }
}