package com.turkcell.subscriptionservice.application.outbox.data;

import com.turkcell.subscriptionservice.domain.constants.OutboxStatus;
import com.turkcell.subscriptionservice.infrastructure.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxDataManager {

    private final OutboxRepository repository;

    @Transactional
    public void updateStatusBulk(List<Long> ids, OutboxStatus status) {
        if (ids == null || ids.isEmpty()) return;
        repository.updateStatusByIds(ids, status, LocalDateTime.now());
    }

    @Transactional
    public void markAsProcessing(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        repository.updateStatusByIds(ids, OutboxStatus.PROCESSING, LocalDateTime.now());
    }
}