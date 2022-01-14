package com.example.ordermodule.controller;


import com.example.ordermodule.dto.OrderDto;
import com.example.ordermodule.dto.PaymentDto;
import com.example.ordermodule.entity.Cart;
import com.example.ordermodule.entity.Order;
import com.example.ordermodule.repo.OrderRepo;
import com.example.ordermodule.response.RESTResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ordermodule.queue.Config.*;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CartController cartController;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(method = RequestMethod.POST, path = "create")
    public ResponseEntity create(@RequestBody Order order) {
        double totalPrice = 0;
        for (Cart cart : CartController.cartHashMap.values()) {
            totalPrice += cart.getUnitPrice() * cart.getQuantity();
        }
        order.setTotalPrice(totalPrice);
        Order orderSave = new Order();
        try {
            orderSave = orderRepo.save(order);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_ORDER, new OrderDto(orderSave));
            cartController.clear();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return new ResponseEntity<>(
                new RESTResponse.Success()
                        .addData(orderSave)
                        .build(), HttpStatus.OK);
    }

    public Order handlerOrder(PaymentDto paymentDto) {
        Order orderExist = orderRepo.findById(paymentDto.getOrderId()).orElse(null);
        if (orderExist == null) {
            System.out.println("Order not found.");
            return null;
        }

        if (paymentDto.getCheckOut() == 1) {
            System.out.println("Order đã thanh toán thành công");
            return orderExist;
        }

        try {
            System.out.println("Order đã thanh toán thành công");
            orderExist.setCheckOut(1);
            return orderRepo.save(orderExist);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
