package com.tramites.inventario.Configuration.Queues.Events;
import com.tramites.inventario.DTO.Component.DeleteDetailEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EvenProducer {
    @Value("${rabbit.queue.event}")
    private String queueEvent;
    private final RabbitTemplate rabbitTemplate;
    public EvenProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendEvent(DeleteDetailEvent deleteDetailEvent) {

        rabbitTemplate.convertAndSend("",queueEvent, deleteDetailEvent);
    }


}
