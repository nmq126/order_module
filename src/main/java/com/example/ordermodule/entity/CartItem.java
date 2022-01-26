package com.example.ordermodule.entity;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartItem {

    private Long productId;
    private String name;
    private int quantity;
    private BigDecimal unitPrice;

}
