# System Architecture

This project is part of a distributed microservices ecosystem built using Spring Boot, RabbitMQ, Apache Kafka, Apache Camel, CDC, Kafka Streams, and Python background workers.
The platform combines synchronous APIs and event-driven communication patterns to achieve scalability, resiliency, and loose coupling between services.

## Components

### Inventory Service

The Inventory Service manages product availability, inventory updates, stock movements, and inventory-related business operations. 
The service exposes REST endpoints for synchronous operations and publishes business events through RabbitMQ to support asynchronous processing across the platform.

 
Key Technologies:

 
- Spring Boot
- JDBC Template
- RabbitMQ
- Postgress / Relational Database

 
### Payments Service

The Payments Service handles payment processing, transaction management, and payment lifecycle events.
The service communicates with other microservices through REST APIs and RabbitMQ events while persisting data through JDBC Template.

 
Key Technologies:
 
- Spring Boot
- JDBC Template
- RabbitMQ
- Postgress / Relational Database

### Kafka Event Router
The Kafka Event Router is an enterprise integration component responsible for consuming events from Kafka and distributing them across multiple downstream systems using Apache Camel.
The service leverages parallel processing, SEDA queues, Circuit Breakers, and Dead Letter Queues (DLQs) to provide a resilient and scalable event routing solution.


Key Technologies:

- Apache Camel
- Apache Kafka
- Resilience4j
- Spring Boot
 
## Event Processing Flow
 
The ecosystem follows an event-driven architecture:
 
1. Inventory and Payments services generate business events.
2. Events are published to RabbitMQ.
3. Background Python workers consume RabbitMQ messages.
4. Python workers execute database stored procedures.
5. Database changes are captured using Change Data Capture (CDC).
6. CDC publishes changes to Apache Kafka.
7. Kafka Streams and KTables process and enrich event data.
8. Apache Camel consumes Kafka events and routes them to downstream consumers.
 
## Architecture Diagram
 
```text

+----------------+.       +----------------+
|   Inventory     | <---> |.   Payments    |
+----------------+        +----------------+
       |                        |
       +-----------+------------+
                   |
                   v
               RabbitMQ
                   |
                   v
         +-------------------+
         | Python Workers    |
         +-------------------+
                   |
                   v
          Stored Procedures
                  |
                  v

            SQL Database
                  |
                  v  
                 CDC
                  |
                  v
              Kafka Topics
                  |
                  v
         Kafka Streams / KTables
                  |
                  v
          Kafka Event Router
           (Apache Camel)
                  |
       +----------+----------+
       |          |          |
       v.         v          v
    System A  System B.  System C

```

## Architecture Goals

- Loose coupling between services
- Event-driven communication
- High scalability
- Fault tolerance
- Resilient integrations
- Asynchronous processing
- Real-time data propagation
- Enterprise integration patterns