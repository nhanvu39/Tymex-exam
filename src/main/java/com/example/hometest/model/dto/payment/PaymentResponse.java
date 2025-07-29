package com.example.hometest.model.dto.payment;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String transactionId;
    private String status;
    private String message;
}
