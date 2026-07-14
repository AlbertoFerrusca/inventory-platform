## Architecture Overview
The Inventory Platform is an event-driven and service-oriented solution designed to manage inventory operations, product distribution, business transactions, and cross-environment data integration.

The platform combines synchronous microservices, asynchronous event processing, PostgreSQL persistence, Python processing services, and Kafka-based integrations to support operational and analytical workloads.

Inventory data is maintained and distributed by state, region, and branch. Database changes are captured through Change Data Capture (CDC) mechanisms and published to Kafka topics, enabling real-time processing, aggregation, and information sharing across multiple environments.

The solution supports inventory management, batch processing, analytics generation, and external system integrations while maintaining scalability, traceability, and operational reliability.

Postman / Clients

│
▼
Microservices
(Synchronous & Event-Driven)

│
▼
Queue
│
▼
Python Processing2
│
▼
PostgreSQL
│
├── Tables
├── Views
├── Procedures
├── Functions
└── Batch Jobs
│
▼
CDC
│
▼
Kafka Topics
│
▼
Kafka Streams0
│
▼
State Stores3
│
▼
Sink Connectors6
│
├── Analytics Databases8
├── Reporting Databases
|-- Statistics Repositories
└── External Environments

## Technologies
- PostgreSQL
- Kafka
- Kafka Connect
- CDC
- Python
- Shell Scripting
- REST APIs
- Event-Driven Architecture
- k6 Performance Testing
- GitHub

