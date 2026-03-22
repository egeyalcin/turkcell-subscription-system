package com.turkcell.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.event.PaymentResponseEvent;
import com.turkcell.event.SubscriptionCreatedEvent;
import com.turkcell.paymentservice.entity.Payment;
import com.turkcell.paymentservice.constants.PaymentStatus;
import com.turkcell.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMockProcessor {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String errMessage = "Yetersiz bakiye nedeniyle işlem reddedildi.";

    private static final String errCodeMessage = "ERR_INSUFFICIENT_FUNDS";

    @KafkaListener(
            topics = "subscription-events",
            groupId = "payment-service-group"
    )
    @Transactional
    public void processSubscriptionEvent(String message) throws JsonProcessingException {
        SubscriptionCreatedEvent event = objectMapper.readValue(message, SubscriptionCreatedEvent.class);
        log.info("Received subscription event for CorrelationID: {}", event.getCorrelationId());
        boolean isSuccess = ThreadLocalRandom.current().nextInt(100) >= 33;
        
        String trxRef = "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String errCode = isSuccess ? null : errCodeMessage;
        String errMsg = isSuccess ? null : errMessage;

        Payment payment = Payment.builder()
                .subscriptionId(event.getSubscriptionId())
                .userId(event.getUserId())
                .amount(event.getPrice())
                .currency("TRY")
                .status(isSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .transactionReference(trxRef)
                .errorCode(errCode)
                .errorMessage(errMsg)
                .build();

        paymentRepository.save(payment);
        log.info("Payment saved to DB with status: {}", payment.getStatus());

        PaymentResponseEvent responseEvent = PaymentResponseEvent.builder()
                .correlationId(event.getCorrelationId())
                .success(isSuccess)
                .subscriptionId(event.getSubscriptionId().toString())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .errorCode(payment.getErrorCode())
                .errorMessage(payment.getErrorMessage())
                .processedAt(LocalDateTime.now())
                .transactionReference(payment.getTransactionReference())
                .build();


        String responseJson = objectMapper.writeValueAsString(responseEvent);
        kafkaTemplate.send("payment-response", responseEvent.getCorrelationId(), responseJson);
        log.info("PaymentResponseEvent sent to topic 'payment-response' for CorrelationID: {}", responseEvent.getCorrelationId());
    }
}