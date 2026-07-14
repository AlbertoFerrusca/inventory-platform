package com.tramites.inventario.Configuration.Queues.OrdersIN;

import com.tramites.inventario.DTO.CompraRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbit.queue.orders}")
    private String queueOrders;
    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(CompraRequestDTO order) {

        rabbitTemplate.convertAndSend("",queueOrders, order);
    }


}
