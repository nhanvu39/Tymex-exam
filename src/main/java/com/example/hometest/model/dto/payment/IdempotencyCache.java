package com.example.hometest.model.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyCache {
    private String requestHash;
    private PaymentResponse response;
}
