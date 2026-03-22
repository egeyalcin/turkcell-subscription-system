package com.turkcell.subscriptionservice.domain.entity;

import com.turkcell.subscriptionservice.domain.constants.SubscriptionStatus;
import com.turkcell.subscriptionservice.domain.constants.SubscriptionType;
import com.turkcell.subscriptionservice.domain.exception.SubscriptionProcessException;
import com.turkcell.subscriptionservice.domain.valueobjects.SubscriptionPeriod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private SubscriptionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private SubscriptionStatus status;

    @Embedded
    private SubscriptionPeriod period;

    @Column(name = "correlation_id", nullable = false, unique = true, updatable = false)
    @Builder.Default
    private UUID correlationId = UUID.randomUUID();

    @Column(name = "error_message", length = 255)
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void activate(int durationMonths) {
        if (this.status != SubscriptionStatus.PENDING_PAYMENT) {
            if (this.status == SubscriptionStatus.ACTIVE) return;
            throw new SubscriptionProcessException("subscription.invalid.status.for.activation");
        }

        this.status = SubscriptionStatus.ACTIVE;
        this.errorMessage = null;

        LocalDateTime start = LocalDateTime.now();
        this.period = SubscriptionPeriod.create(start, durationMonths);
    }

    public void fail(String reason) {
        if (this.status == SubscriptionStatus.CANCELLED || this.status == SubscriptionStatus.ACTIVE) {
            return;
        }
        this.status = SubscriptionStatus.REJECTED;

        this.errorMessage = (reason != null && !reason.isBlank())
                ? reason
                : "unknown.payment.error";
    }

    public void cancel() {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new SubscriptionProcessException("subscription.already.cancelled");
        }

        if (this.status == SubscriptionStatus.PENDING_PAYMENT) {
            this.status = SubscriptionStatus.CANCELLED;
        } else {
            this.status = SubscriptionStatus.CANCELLED;
            this.period = new SubscriptionPeriod(this.period.getStartDate(), LocalDateTime.now());        }
    }
}
