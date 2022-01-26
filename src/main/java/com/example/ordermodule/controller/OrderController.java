package com.example.ordermodule.controller;


import com.example.ordermodule.dto.PaymentDto;
import com.example.ordermodule.entity.Order;
import com.example.ordermodule.service.OrderService;
import com.example.ordermodule.specification.SearchBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @RequestMapping(method = RequestMethod.POST, path = "create")
    public ResponseEntity create(@RequestBody Order order) {
       return orderService.createOrder(order);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllOrders(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "id", required = false) Long id
    ) throws Exception {
        SearchBody searchBody = SearchBody.SearchBodyBuilder.aSearchBody()
                .withPage(page)
                .withLimit(limit)
                .withId(id)
                .build();
        return orderService.findAll(searchBody);
    }
}
