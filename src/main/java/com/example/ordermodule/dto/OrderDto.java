package com.example.ordermodule.dto;

import com.example.ordermodule.entity.Order;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private String paymentStatus;
    private String inventoryStatus;
    private BigDecimal totalPrice;
    private HashMap<Long, Integer> products;
    private String device_token;

    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.totalPrice = order.getTotalPrice();
        this.paymentStatus = order.getPaymentStatus();
        this.orderStatus = order.getOrderStatus();
        this.inventoryStatus = order.getInventoryStatus();
    }
}
