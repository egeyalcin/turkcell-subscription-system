package com.turkcell.subscriptionservice.presentation;

import com.turkcell.subscriptionservice.application.subscription.service.SubscriptionService;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionCreateRequest;
import com.turkcell.subscriptionservice.domain.dto.SubscriptionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> createSubscription(
            @Valid @RequestBody SubscriptionCreateRequest request) {
        
        SubscriptionResponseDTO response = subscriptionService.createSubscription(request);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSubscription(@PathVariable UUID id) {
        subscriptionService.cancelSubscription(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getSubscription(@PathVariable UUID id) {
        return ResponseEntity.ok(subscriptionService.getSubscription(id));
    }
}