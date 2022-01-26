package com.example.ordermodule.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {

    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;
    private String paymentStatus;
    private String message;
    private String device_token;
}
