# Kafka Event Router with Apache Camel
A high-performance event-driven integration solution built with Apache Camel, Apache Kafka, and Spring Boot.

This application consumes events from Kafka and distributes them across multiple independent processing pipelines using Multicast and parallel processing, enabling scalable and resilient event delivery to downstream systems.

To decouple workloads and improve throughput, each processing flow is backed by dedicated SEDA queues, allowing asynchronous processing and preventing slow consumers from impacting the entire system.

The solution incorporates Resilience4j Circuit Breakers to protect downstream services from cascading failures. When an endpoint becomes unavailable, messages are automatically redirected to dedicated Dead Letter Queues (DLQs) for later analysis and recovery.

## Key Features

- Event consumption from Apache Kafka
- Parallel event distribution using Apache Camel Multicast
- Asynchronous processing with SEDA queues
- JSON-to-XML transformation support
- Circuit Breaker pattern implementation with Resilience4j
- Dead Letter Queue (DLQ) strategy for failed messages
- Externalized configuration through Spring Boot properties
- Configurable concurrency and performance tuning options
- Production-oriented architecture focused on scalability and resiliency
## Architecture

```text
                                 Kafka
                                   |
                                   v
                      +-----------------------------+
                      |       KafkaEventRouter      |
                      +-----------------------------+
                                    |
                       Multicast + ParallelProcessing
                                    |
                     +--------------+--------------+
                     |              |              |
                     v              v              v
                 seda:xml       seda:json.uno  seda:json.dos
                     |              |              |
                     v              v              v
               marshal XML   Endpoint JSON1   Endpoint JSON2
                     |              |              |
                     v              v              v
                DLQ XML          DLQ JSON1     DLQ JSON2
...                
```

## Technology Stack

- Java 21
- Spring Boot
- Apache Camel
- Apache Kafka
- Resilience4j
- Maven

## Future Improvements

- Integration tests with Testcontainers
- End-to-end Kafka testing
- Metrics and observability
- Advanced retry policies
- Docker support
- Kubernetes deployment

## Author
Inspired by my children, who remind me every day that learning never stops.
Santiago & Leo Ferrusca Vargas




