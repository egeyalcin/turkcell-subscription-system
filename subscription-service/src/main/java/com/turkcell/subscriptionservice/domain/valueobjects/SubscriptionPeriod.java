package com.turkcell.subscriptionservice.domain.valueobjects;

import com.turkcell.subscriptionservice.domain.exception.SubscriptionProcessException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SubscriptionPeriod {
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static SubscriptionPeriod create(LocalDateTime start, int months) {
        if (start == null) throw new SubscriptionProcessException("Start date cannot be empty");
        return new SubscriptionPeriod(start, start.plusMonths(months));
    }

}