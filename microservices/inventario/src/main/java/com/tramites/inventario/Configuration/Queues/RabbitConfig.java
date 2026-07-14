package com.tramites.inventario.Configuration.Queues;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {
    @Value("${rabbit.queue.orders}")
    private String queueOrders;
    @Value("${rabbit.queue.event}")
    private String queueEvent;
    @Bean
    public Queue queueDeleteItems() {
        return new Queue(queueEvent, true);
    }
    @Bean
    public Queue orderQueue() {
        return new Queue(queueOrders, true);
    }
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
