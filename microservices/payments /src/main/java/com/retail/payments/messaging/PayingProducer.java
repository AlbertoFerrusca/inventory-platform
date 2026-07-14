package com.retail.payments.messaging;

import com.retail.payments.DTO.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class PayingProducer {
    private static final Logger log = LoggerFactory.getLogger(PayingProducer.class);
    @Value("${rabbit.queue.payment}")
    private String QueueName;
    private final RabbitTemplate rabbitTemplate;

    public PayingProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPayment(PaymentRequest request) {
        rabbitTemplate.convertAndSend("",QueueName, request);
    }
}
