package com.example.ordermodule.queue;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example.ordermodule.queue.Config.QUEUE_PAY;

@Component
public class ReceiveMessage {

    @RabbitListener(queues = {QUEUE_PAY})
    public void getInfoPayment(String message) {
        System.out.println("Nhận thông tin thanh toán thành công: " + message);
    }

    @RabbitListener(queues = {QUEUE_PAY})
    public void getInfoOrder(String message) {
        System.out.println("Nhận thông tin order thành công: " + message);
    }
}
