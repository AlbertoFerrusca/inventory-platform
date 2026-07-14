package com.retail.payments.integracion;

import com.retail.payments.DTO.PaymentRequest;
import com.retail.payments.messaging.PayingProducer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"payments-topic-JAL"})
public class KafkaIntegrationTest {

    @Autowired
    private PayingProducer producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;


    @Test
    void shouldSendMessageToKafka() {

        // ✅ Crear request
        PaymentRequest request = new PaymentRequest();
        request.setOrder_id("123");

        // ✅ Enviar mensaje
        producer.sendPayment(request);

        // ✅ Crear consumidor
        Map<String, Object> props = KafkaTestUtils.consumerProps(
                "test-group", "true", embeddedKafka);

        Consumer<String, String> consumer =
                new org.apache.kafka.clients.consumer.KafkaConsumer<>(
                        props,
                        new StringDeserializer(),
                        new StringDeserializer()
                );

        consumer.subscribe(java.util.List.of("payments-topic-JAL"));

        // ✅ Leer mensajes
        ConsumerRecords<String, String> records =
                KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));

        // ✅ Validar que llegó el mensaje
        assertFalse(records.isEmpty());

        records.forEach(record -> {
            System.out.println("Mensaje recibido: " + record.value());
            assertTrue(record.value().contains("123"));
        });

        consumer.close();
    }

}
