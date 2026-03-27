# Winery Management System

## Project Description

Winery Management System is a microservice-based Spring Boot application for winery operations management.
The goal is to support the complete flow from grape harvest to wine production, inventory handling, and customer sales.

The system covers:
- yearly harvest evidence (grape type, vineyard, date, quantity, quality),
- wine recipe and production planning,
- bottle inventory and reservation management,
- customer profiles, purchases, and wine reviews.

The system is split into 4 executable Maven modules:

1. `customer-service`
Handles customer-related operations:
- customer profile management,
- purchase history,
- wine reviews and feedback.

2. `harvest-service`
Handles harvest domain:
- vineyards and grape types management,
- harvest records,
- harvest summaries.

3. `production-service`
Handles production domain:
- wine recipe management,
- production planning from available harvest lots,
- blend quality evaluation,
- production batch lifecycle.

4. `inventory-service`
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
- production feasibility planning from recipe composition and available harvest lots (requested vs feasible bottle count, producible liters, limiting grape type),
- weighted blend quality evaluation with recommendation output based on quality target,
- creation of planned production batches from computed production plan.

4. `inventory-service`
- stock removal guarded by available (non-reserved) quantity checks,
- reservation status transitions with stock/reserved quantity adjustments,
- stock lookup by wine and reservation filtering by customer/wine/date interval.

## Executable Modules

Executable Spring Boot modules:
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
- in-memory repositories for M1 persistence

## Build And Run

For Milestone 1, the project uses in-memory data storage and does not rely on JPA/database persistence.

Build whole project:

```bash
mvn clean install
```

Run one service module (example):

```bash
mvn -pl harvest-service spring-boot:run
```

You can run other modules analogically:

```bash
mvn -pl customer-service spring-boot:run
mvn -pl production-service spring-boot:run
mvn -pl inventory-service spring-boot:run
```

Alternatively, you can enter a concrete service module directory and run:

```bash
mvn spring-boot:run
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

GenAI tools were used as an implementation support aid (code review assistance, refactoring suggestions, and documentation drafting).
All generated suggestions were manually reviewed and adjusted by team members before integration.
