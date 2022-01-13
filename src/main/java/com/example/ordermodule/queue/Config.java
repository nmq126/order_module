package com.example.ordermodule.queue;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class Config {


    public static final String QUEUE_ORDER = "direct.queue.order";
    public static final String QUEUE_PAY = "direct.queue.pay";

    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String ROUTING_KEY_DIRECT = "direct.routingKey";


    @Bean
    public Declarables directBinding() {
        Queue directQueue = new Queue(QUEUE_ORDER);
        DirectExchange directExchange = new DirectExchange(DIRECT_EXCHANGE);
        return new Declarables(
                directQueue,
                directExchange,
                bind(directQueue).to(directExchange).with(ROUTING_KEY_DIRECT)
        );
    }

}
