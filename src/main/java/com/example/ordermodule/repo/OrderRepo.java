package com.example.ordermodule.repo;

import com.example.ordermodule.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
