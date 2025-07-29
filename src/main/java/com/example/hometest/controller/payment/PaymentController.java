package com.example.hometest.controller.payment;

import com.example.hometest.model.dto.payment.PaymentRequest;
import com.example.hometest.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> processPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody PaymentRequest request) throws Exception {

            return paymentService.processPayment(idempotencyKey, request);
    }
}
