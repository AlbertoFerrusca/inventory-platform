package com.tramites.inventario.Configuration.Queues.OrdesOut;

import com.tramites.inventario.DTO.Component.HeadDetail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService {

    @RabbitListener(queues = "delete-items-queue")
    public void receive(List<HeadDetail> items) {

        System.out.println("Recibiendo batch 🔥: " + items.size());

        // aquí llamas tu método batchUpdate
        //repository.deteleitems(items);
    }

}
