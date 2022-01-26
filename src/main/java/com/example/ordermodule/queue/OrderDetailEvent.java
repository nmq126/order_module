package com.example.ordermodule.queue;

import com.example.ordermodule.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEvent {

    private Long productId;
    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderDetailEvent(OrderDetail orderDetail) {
        this.productId = orderDetail.getProductId();
        this.orderId = orderDetail.getOrderId();
        this.productName = orderDetail.getProductName();
        this.quantity = orderDetail.getQuantity();
        this.unitPrice = orderDetail.getUnitPrice();
    }
}