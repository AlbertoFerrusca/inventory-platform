package com.tramites.inventario.Configuration.Queues.OrdesOut;

import com.tramites.inventario.DTO.Component.HeadDetail;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;

    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(List<HeadDetail> items) {
        rabbitTemplate.convertAndSend("delete-items-queue", items);
    }


}
