package com.example.ordermodule.service;

import com.example.ordermodule.dto.PaymentDto;
import com.example.ordermodule.entity.Order;
import com.example.ordermodule.specification.SearchBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {

    ResponseEntity createOrder(@RequestBody Order order);
    ResponseEntity findAll(@RequestBody SearchBody searchBody) throws Exception;
}
