package com.inventory.kafka_event_router_camel.components;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventRouter  extends RouteBuilder {

    @Value("${mi.topico.entrada}")
    private String topicoEntrada;

    @Value("${kafka.group.xml}")
    private String grupoXml;

    @Value("${endpoint.salida.xml}")
    private String endpointXml;

    @Value("${endpoint.salida.json.uno}")
    private String endpointJsonUno;

    @Value("${endpoint.salida.json.dos}")
    private String endpointJsonDos;


    public void configure() throws Exception{

        // ===================================================================
        // RUTA PRINCIPAL: CONSUMIDOR DE KAFKA Y REPARTO ASÍNCRONO (SEDA)
        // ===================================================================
        from("kafka:{{mi.topico.entrada}}"+"?groupId={{kafka.group.router}}&maxPollRecords={{kafka.max.poll}}")
                .routeId("Kafka-Consumer-Main")
                .log("Mensaje recibido de Kafka, enviando a colas SEDA...")
                // Multicast duplica el mensaje y parallelProcessing lo envía de forma asíncrona
                .multicast().parallelProcessing()
                .to("seda:{{cola.procesar.xml}}")
                .to("seda:{{cola.procesar.json.uno}}")
                .to("seda:{{cola.procesar.json.dos}}")
                .end();

        from ("seda:cola.procesar.xml?concurrentConsumers={{seda.concurrent.consumers}}")
                .routeId("Procesador-SEDA-XML")
                .log("Ruta SEDA XML procesando mensaje")
                .marshal().jacksonXml()
                .circuitBreaker()
                .to(endpointXml)
                .onFallback()
                .log("¡Circuito Abierto o Error! Ejecutando Fallback para XML")
                .to("kafka:{{eventos.dlq.xml}}")
                .end()
        .end();

        // ===================================================================
        // PROCESADOR 2: COLA SEDA -> ENDPOINT 2 (JSON)
        // ===================================================================
        from("seda:cola.procesar.json.uno?concurrentConsumers={{seda.concurrent.consumers}}")
                .routeId("Procesador-SEDA-JSON-1")
                .log("Ruta SEDA JSON 1 procesando mensaje")
                .circuitBreaker()
                .to(endpointJsonUno) // Endpoint Final 2 (JSON 1)
                .onFallback()
                .log("Fallback JSON 1: Endpoint 2 caído. Guardando en DLQ.")
                .to("kafka:{{eventos.dlq.json1}}")
                .end();

        // ===================================================================
        // PROCESADOR 3: COLA SEDA -> ENDPOINT 3 (JSON)
        // ===================================================================
        from("seda:cola.procesar.json.dos?concurrentConsumers={{seda.concurrent.consumers}}")
                .routeId("Procesador-SEDA-JSON-2")
                .log("Ruta SEDA JSON 2 procesando mensaje")
                // No requiere transformación por que ya viene originalmente en JSON
                .circuitBreaker()
                .to(endpointJsonDos) // Endpoint Final 3 (JSON 2)
                .onFallback()
                .log("Fallback JSON 2: Endpoint 3 caído. Guardando en DLQ.")
                .to("kafka:{{eventos.dlq.json2}}")
                .end();

    }
    
}
