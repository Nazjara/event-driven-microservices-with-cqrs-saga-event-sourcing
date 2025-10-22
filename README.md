# Event-Driven Microservices With CQRS, Saga, and Materialized Views

Learning project demonstrating microservice patterns: CQRS, Saga, Event Sourcing, and Materialized Views.

## Modules

- **base** - Basic microservices architecture with REST APIs
- **cqrs** - CQRS pattern implementation with Axon Framework
- **materialized-view** - Materialized view pattern for read optimization
- **saga-choreography** - Saga pattern with choreography approach
- **saga-orchestration** - Saga pattern with orchestration approach

Each module contains independent microservices (accounts, cards, loans, customer, gateway, discovery).

## Build

Build parent POM first:
```bash
cd <module-name>/parent-pom
../mvnw clean install
```

Build services:
```bash
cd <module-name>/<service-name>
../mvnw clean install
```

Or use a rebuild script:
```bash
cd <module-name>
./rebuild.sh
```

## Run

```bash
cd <module-name>/<service-name>
../mvnw spring-boot:run
```
