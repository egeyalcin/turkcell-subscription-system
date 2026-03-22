package com.turkcell.subscriptionservice.infrastructure;

import com.turkcell.subscriptionservice.domain.constants.OutboxStatus;
import com.turkcell.subscriptionservice.domain.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {

    List<OutboxMessage> findTop500ByStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE OutboxMessage o SET o.status = :status, o.processedAt = :now WHERE o.id IN :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids,
                           @Param("status") OutboxStatus status,
                           @Param("now") LocalDateTime now);

    @Query(value = """
    SELECT * FROM outbox_messages 
    WHERE status = 'FAILED' 
      AND created_at >= NOW() - INTERVAL '2 minutes'
    ORDER BY created_at ASC
    """, nativeQuery = true)
    List<OutboxMessage> findFailedMessagesInLastTwoMinutes();
}
