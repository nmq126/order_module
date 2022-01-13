package com.example.ordermodule.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ordermodule.queue.Config.DIRECT_EXCHANGE;
import static com.example.ordermodule.queue.Config.ROUTING_KEY_DIRECT;

@RestController
@RequestMapping("")
public class SentMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/createOrder")
    public String sendMessage(@RequestBody com.example.demorabitmq.dto.OrderDto messageDto) {
        String message = messageDto.getMessage();
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, ROUTING_KEY_DIRECT, message);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }

}
