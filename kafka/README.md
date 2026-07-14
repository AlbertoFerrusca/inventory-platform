## Purpose
This directory contains Kafka components used by the inventory management platform for event streaming, data integration, inventory synchronization, and analytics distribution.
## Architecture Role
Kafka serves as the event backbone of the platform, enabling asynchronous communication between services and supporting real-time data processing.
## Components
### Topics
Topics are generated and populated through Change Data Capture (CDC) connectors and application events.
Topics represent business events and database changes that can be consumed by downstream services.
### Streams
Kafka Streams applications process incoming events through:
- Filtering
- Aggregation
- Transformation
- Enrichment
### State Stores
State Stores maintain processing state required by stream applications.
Examples include:
- Inventory State
- Product State
- Regional Inventory State
- Branch Inventory State
### Sink Connectors
Sink Connectors publish processed Kafka data into target systems such as:
- PostgreSQL
- Reporting platforms
- Analytics environments
- External systems
### Schemas
Schemas define the structure of messages exchanged between producers and consumers.
## Data Flow
PostgreSQL Tables
CDC Connectors
Kafka Topics
Kafka Streams
State Stores
Sink Connectors
PostgreSQL / External Systems
## Business Functions
Kafka supports:
- Inventory synchronization
- Product updates
- Regional inventory distribution
- Branch inventory distribution
- Statistical data generation
- Event-driven workflows
- Cross-environment integration
## Related Components
- PostgreSQL
- Microservices
- Python Services
- Batch Processes
- Shell Scripts
## Notes
Kafka topics, stream definitions, state stores, schemas, and connector configurations should be maintained under source control to ensure traceability and consistency across environments.
