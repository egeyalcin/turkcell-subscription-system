package com.turkcell.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEvent {

    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    private String correlationId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}