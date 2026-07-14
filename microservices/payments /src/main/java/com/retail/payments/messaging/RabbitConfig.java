package com.retail.payments.messaging;
//import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitConfig {
    @Value("${rabbit.queue.payment}")
    private String queueName;
    @Bean
    public Queue queueDeleteItems() {
        return new Queue(queueName, true);
    }
    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();

    }


}
