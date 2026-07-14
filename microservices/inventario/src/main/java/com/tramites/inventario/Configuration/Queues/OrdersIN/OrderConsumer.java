package com.tramites.inventario.Configuration.Queues.OrdersIN;

import com.tramites.inventario.DTO.CompraRequestDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @RabbitListener(queues = "orders-queue")
    public void receive(CompraRequestDTO order) {

        System.out.println("📦 Recibiendo orden: " + order.getOrderId());

        for ( var item : order.getItems()) {
            System.out.println("Producto: " + item.getProductoId());
        }

        // aquí llamas tu lógica:
        // repository.saveOrder(order);
    }

}
