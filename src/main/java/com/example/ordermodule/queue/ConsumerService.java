package com.example.ordermodule.queue;

import com.example.ordermodule.entity.Order;
import com.example.ordermodule.enums.Status;
import com.example.ordermodule.repo.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.ordermodule.queue.Config.*;

@Service
@Log4j2
public class ConsumerService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public void handleMessage(OrderEvent orderEvent) {
        System.out.println(orderEvent.getQueueName());
        System.out.println("dcm" + orderEvent.getInventoryStatus());
        Order orderFound = orderRepository.findById(orderEvent.getOrderId()).orElse(null);
        System.out.println(orderFound.toString());
        if (orderFound == null) {
            orderEvent.setMessage("Order not found.");
            return;
        }

        if (orderEvent.getQueueName().equals(QUEUE_PAY)) {
            orderFound.setPaymentStatus(orderEvent.getPaymentStatus());
        }

        if (orderEvent.getQueueName().equals(QUEUE_INVENTORY)) {
            orderFound.setInventoryStatus(orderEvent.getInventoryStatus());
            System.out.println("ax" + orderEvent.getInventoryStatus());
        }
        handleOrder(orderFound);
    }

    private void handleOrder(Order order) {
        if (order.getPaymentStatus().equals(Status.PaymentStatus.DONE.name())
                && order.getInventoryStatus().equals(Status.InventoryStatus.OUT_OF_STOCK.name())) {
            order.setPaymentStatus(Status.PaymentStatus.REFUND.name());
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_PAY, new OrderEvent(order));
        }
        if (order.getInventoryStatus().equals(Status.InventoryStatus.DONE.name())
                && order.getPaymentStatus().equals(Status.PaymentStatus.NOT_ENOUGH_BALANCE.name())) {

            order.setInventoryStatus(Status.InventoryStatus.RETURN.name());
            orderRepository.save(order);

            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_ROUTING_KEY_INVENTORY, new OrderEvent(order));
        }
    }

}

