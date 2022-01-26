package com.example.ordermodule.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @Column(name = "total_price")
    private BigDecimal totalPrice;
    private String address;
    private String phone;
    private String email;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "inventory_status")
    private String inventoryStatus;

    @Column(name = "order_status")
    private String orderStatus;

}
