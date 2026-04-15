# Winery Management System

## Project Description

Winery Management System is a microservice-based Spring Boot application for winery operations management.
The goal is to support the complete flow from grape harvest to wine production, inventory handling, and customer sales.

The system covers:

- yearly harvest evidence (grape type, vineyard, date, quantity, quality),
- wine recipe and production planning,
- bottle inventory and reservation management,
- customer profiles, purchases, and wine reviews.

The system is split into 5 executable Maven modules:

1. `common-service`
   Handles shared application concerns:

- shared exceptions and error handling,
- reusable API/common classes,
- code extracted from multiple services during refactoring.

2. `customer-service`
   Handles customer-related operations:

- customer profile management,
- purchase history,
- wine reviews and feedback.

3. `harvest-service`
   Handles harvest domain:

- vineyards and grape types management,
- harvest records,
- harvest summaries.

4. `production-service`
   Handles production domain:

- wine recipe management,
- production planning from available harvest lots,
- blend quality evaluation,
- production batch lifecycle.

5. `inventory-service`
   Handles stock domain:

- bottle stock tracking,
- reservation tracking,
- available/reserved quantity changes.

## Non-trivial Business Functionalities

Implemented business functionality beyond CRUD:

1. `customer-service`

- purchase history filtering by quantity interval (`min`/`max`) with input normalization,
- total spending computation per customer (sum of quantity x price per bottle),
- customer/wine based review and purchase history retrieval workflows.

2. `harvest-service`

- harvest quantity aggregation by year (`summary/by-year`),
- harvest quantity aggregation by vineyard (`summary/by-vineyard`),
- domain validation linking harvest records to existing vineyard and grape type entities.

3. `production-service`

- production feasibility planning from recipe composition and available harvest lots (requested vs feasible bottle
  count, producible liters, limiting grape type),
- weighted blend quality evaluation with recommendation output based on quality target,
- creation of planned production batches from computed production plan.

4. `inventory-service`

- stock removal guarded by available (non-reserved) quantity checks,
- reservation status transitions with stock/reserved quantity adjustments,
- stock lookup by wine and reservation filtering by customer/wine/date interval.

## Executable Modules

Executable Spring Boot modules:

- `common-service`
- `customer-service`
- `harvest-service`
- `production-service`
- `inventory-service`

## Technology Stack

The project is built with:

- Java
- Spring Boot
- Maven
- MapStruct
- springdoc-openapi
- Spring Data JPA
- H2 in-memory database
- Docker

## Build And Run

For Milestone 2, the project gradually connects the REST API to the service and persistence layers.
Services use Spring Data JPA with an H2 in-memory database.

Build whole project:

```bash
mvn clean install
```

Run production-service tests with its local module dependencies:

```bash
mvn -pl production-service -am test
```

Run one service module (example):

```bash
mvn -pl harvest-service spring-boot:run
```

You can run other modules analogically:

```bash
mvn -pl common-service spring-boot:run
mvn -pl customer-service spring-boot:run
mvn -pl production-service spring-boot:run
mvn -pl inventory-service spring-boot:run
```

Alternatively, you can enter a concrete service module directory and run:

```bash
mvn spring-boot:run
```

## Docker

Build and run services with Docker Compose:

```bash
docker compose up --build production-service
docker compose up --build harvest-service
```

Stop Docker Compose services:

```bash
docker compose down
```

### Harvest-service

Build the harvest-service Docker image:

```bash
docker build -f harvest-service/Dockerfile -t winery-management/harvest-service:latest .
```

Run the harvest-service container:

```bash
docker run --rm -p 8081:8081 winery-management/harvest-service:latest
```

### Production-service

Build the production-service Docker image:

```bash
docker build -f production-service/Dockerfile -t winery-management/production-service:latest .
```

Run the production-service container:

```bash
docker run --rm -p 8082:8082 winery-management/production-service:latest
```

### Inventory-service

Build the inventory-service Docker image:

Run the inventory-service container:

### Customer-service

Build the customer-service Docker image:

```bash
docker build -f customer-service/Dockerfile -t winery-management/customer-service:latest .
```

Run the customer-service container:
```bash
docker run --rm -p 8084:8084 winery-management/customer-service:latest
```

## Use Case Diagram

PlantUML source: `useCase.puml`
Rendered image:

![Use Case Diagram](diagrams/use-case/useCase.png)

## DTO Class Diagram

DTO class diagrams are available in [`diagrams/class`](diagrams/class):

- `customer-service-dto.puml`
- `harvest-service-dto.puml`
- `production-service-dto.puml`
- `inventory-service-dto.puml`

Rendered `.png` exports are stored next to the PlantUML sources.

### Customer Service

![Customer Service DTO Class Diagram](diagrams/class/customer-service-dto.png)

### Harvest Service

![Harvest Service DTO Class Diagram](diagrams/class/harvest-service-dto.png)

### Production Service

![Production Service DTO Class Diagram](diagrams/class/production-service-dto.png)

### Inventory Service

![Inventory Service DTO Class Diagram](diagrams/class/inventory-service-dto.png)

## GenAI Usage Disclosure

GenAI tools were used as an implementation support aid (code review assistance, refactoring suggestions, and
documentation drafting).
All generated suggestions were manually reviewed and adjusted by team members before integration.
