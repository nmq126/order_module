package com.example.ordermodule.queue;


import com.example.ordermodule.dto.PaymentDto;
import com.example.ordermodule.fcm.FCMService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.ordermodule.queue.Config.*;

@Component
public class ReceiveMessage {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private FCMService fcmService;

    @RabbitListener(queues = {QUEUE_ORDER})
    public void handleMessage(OrderEvent orderEvent) {
        consumerService.handleMessage(orderEvent);

//        PnsRequest pnsRequest = new PnsRequest();
//        pnsRequest.setFcmToken(paymentDto.getDevice_token());
//        pnsRequest.setContent(paymentDto.getMessage());
//        pnsRequest.setTitle("Order " + paymentDto.getOrderId());
//        fcmService.pushNotification(pnsRequest);
    }

}
