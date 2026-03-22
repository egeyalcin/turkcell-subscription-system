package com.turkcell.subscriptionservice.infrastructure;

import com.turkcell.subscriptionservice.domain.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    @Query(value = """
        SELECT * FROM subscriptions 
        WHERE status = :status 
        AND CAST(start_date AS DATE) = (CURRENT_DATE - INTERVAL '1 month')
        """, nativeQuery = true)
    List<Subscription> findSubscriptionsToRenew(@Param("status") String status);

    Optional<Subscription> findByCorrelationId(UUID correlationId);
}
