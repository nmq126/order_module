package com.example.ordermodule.queue;

import com.example.ordermodule.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private Long orderId;
    private Long userId;
    private Set<OrderDetailEvent> orderDetailEvents = new HashSet<>();
    private BigDecimal totalPrice;
    private String paymentStatus;
    private String inventoryStatus;
    private String orderStatus;
    private String device_token;
    private String message;
    private String queueName;

    public OrderEvent(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.totalPrice = order.getTotalPrice();
        this.paymentStatus = order.getPaymentStatus();
        this.inventoryStatus = order.getInventoryStatus();
        this.orderStatus = order.getOrderStatus();
        order.getOrderDetails().forEach(orderDetail -> {
            this.orderDetailEvents.add(new OrderDetailEvent(orderDetail));
        });
    }
}
