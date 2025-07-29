package com.example.hometest.service.payment;

import com.example.hometest.model.dto.payment.PaymentRequest;
import com.example.hometest.model.dto.payment.PaymentResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<?> processPayment(String idempotencyKey, PaymentRequest request) throws Exception;
}
