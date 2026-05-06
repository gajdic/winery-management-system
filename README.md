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
- Apache ActiveMQ Artemis (JMS)
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

### Production-service Database Seed

`production-service` uses Liquibase for schema creation. By default it starts with an empty schema so tests and local
development runs are deterministic.

Run `production-service` with realistic seed data:

```bash
mvn -pl production-service -Dspring-boot.run.profiles=seed spring-boot:run
```

PowerShell equivalent:

```powershell
mvn -pl production-service -D"spring-boot.run.profiles"=seed spring-boot:run
```

The seed data is stored in the Liquibase `seed` context and includes realistic wine recipes, grape components,
additional ingredients, and production batches in several lifecycle states. The seed changeset also contains rollback
delete statements, so the seeded data can be cleared by rolling back that Liquibase changeset.

### Harvest-service Database Seed

`harvest-service` uses Liquibase for schema creation. By default it starts with an empty schema.

Run `harvest-service` with realistic seed data:

```bash
mvn -pl harvest-service -Dspring-boot.run.profiles=seed spring-boot:run
```

PowerShell equivalent:

```powershell
mvn -pl harvest-service -D"spring-boot.run.profiles"=seed spring-boot:run
```

The seed data is stored in the Liquibase `seed` context and includes realistic grape varieties, vineyards, and harvest
records across multiple seasons. The seed changeset also contains rollback delete statements, so the seeded data can be
cleared by rolling back that Liquibase changeset.

### Customer-service Database Seed

`customer-service` loads initial data on application startup using `InsertInitialDataService`.

Run `customer-service` with startup seed data:

```bash
mvn -pl customer-service spring-boot:run
```

The startup seed includes customer profiles with addresses, reviews, and purchase records. Because the service uses an
H2 in-memory database, the data is reset by restarting the service.

### Inventory-service Database Seed

`inventory-service` loads initial data on application startup using `InsertInitialDataService`.

Run `inventory-service` with startup seed data:

```bash
mvn -pl inventory-service spring-boot:run
```

The startup seed includes stock records and reservations. Because the service uses an H2 in-memory database, the data is
reset by restarting the service.

## Docker

Build and run services with Docker Compose:

```bash
docker compose up --build
```

`customer-service` and `inventory-service` load their startup seed data automatically. `harvest-service` and
`production-service` use the `seed` Spring profile for Liquibase seed data.

Rebuild from scratch without cache (recommended after messaging changes):

```bash
docker compose build --no-cache
docker compose up
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

Run Docker Compose with seeded `harvest-service` data:

```bash
HARVEST_SERVICE_PROFILES=seed docker compose up --build harvest-service
```

PowerShell equivalent:

```powershell
$env:HARVEST_SERVICE_PROFILES = "seed"
docker compose up --build harvest-service
```

### Inventory-service

Build the inventory-service Docker image:

```bash
docker build -t inventory-service -f .\inventory-service\Dockerfile .
```

Run the inventory-service container:

```bash
docker run -d --name winery-inventory -p 8083:8083 inventory-service
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

Run Docker Compose with seeded `production-service` data:

```bash
PRODUCTION_SERVICE_PROFILES=seed docker compose up --build artemis production-service
```

PowerShell equivalent:

```powershell
$env:PRODUCTION_SERVICE_PROFILES = "seed"
docker compose up --build artemis production-service
```

### Customer-service

Build the customer-service Docker image:

```bash
docker build -f customer-service/Dockerfile -t winery-management/customer-service:latest .
```

Run the customer-service container:

```bash
docker run --rm -p 8084:8084 winery-management/customer-service:latest
```

## Asynchronous Communication 

Implemented message flow:

- `production-service` (producer) sends `BatchCompletedMessage` to queue `batch-completed-queue`
- `inventory-service` (consumer) reads message asynchronously, stores inbound log, and updates stock
- `inventory-service` sends `StockUpdatedMessage` to `stock-updated-queue` (optional feedback event)

Queue names are centralized in `common-service`:

- `batch-completed-queue`
- `stock-updated-queue`

Message persistence:

- Producer outbound log table: `OUTBOUND_MESSAGE_LOG` (`production-service` DB)
- Consumer inbound log table: `INBOUND_MESSAGE_LOG` (`inventory-service` DB)

How to verify messaging end-to-end:

1. Start stack with Docker Compose (`docker compose up --build`).
2. Create/update a production batch and set status to `COMPLETED` in production-service API.
3. Check Artemis console queue metrics (message count/delivering count).
4. Check DB logs in both services (`OUTBOUND_MESSAGE_LOG`, `INBOUND_MESSAGE_LOG`).

## CI Pipeline

The project includes a GitLab CI pipeline configured in [`.gitlab-ci.yml`](.gitlab-ci.yml).

The pipeline is executed for:

- pushes to the `main` branch,
- merge requests.

The pipeline uses the Maven image `maven:3.9.12-eclipse-temurin-25` for Java/Maven jobs and caches the local
Maven repository in `.m2/repository` to speed up repeated builds.

Pipeline stages:

1. `test`
   Runs Maven tests for each executable service module:

- `customer-service`
- `harvest-service`
- `inventory-service`
- `production-service`

Each test job uses:

```bash
mvn -pl "${SERVICE_NAME}" -am test
```

The `-am` flag also builds required dependent modules, for example `common-service`, so every service is tested
with the modules it needs.

2. `build`
   Verifies that every executable service can be built as a Maven module:

```bash
mvn -pl "${SERVICE_NAME}" -am -DskipTests -Dmaven.test.skip=true clean install
```

Tests are intentionally skipped in this stage because they already run in the previous `test` stage. This stage
checks packaging and module build correctness.

3. `docker_build`
   Verifies Docker image builds for all executable services:

- `customer-service/Dockerfile`
- `harvest-service/Dockerfile`
- `inventory-service/Dockerfile`
- `production-service/Dockerfile`

Docker builds are performed with Kaniko using `--no-push`, so CI validates the Dockerfiles without publishing
images to a container registry.

In short, the CI pipeline checks that every service:

- passes its Maven tests,
- can be built together with required module dependencies,
- has a Dockerfile that can be built successfully.

Container runtime validation with Docker Compose is currently intended to be performed locally, because the
current GitLab runner setup does not provide a working Docker daemon for `docker compose up` / `docker run`
checks inside CI jobs.

Recommended local Docker verification commands:

```bash
docker compose up --build
docker compose down
```

## Security

The `customer-service`, `harvest-service`, `production-service`, and `inventory-service` are configured as OAuth 2
Resource Servers based on the PA165 seminar security setup. They validate MUNI-issued JWT access tokens from
`https://id.muni.cz/oidc/`.

Customer API authorization rules:

- `GET /customers/**`, `GET /purchases/**`, and `GET /reviews/**` require scope `test_read`.
- `POST`, `PUT`, and `DELETE` operations on customer, purchase, and review endpoints require scope `test_write`.

Harvest API authorization rules:

- `GET /vineyards/**`, `GET /grape-types/**`, and `GET /harvest-records/**` require scope `test_read`.
- `POST`, `PUT`, and `DELETE` operations on harvest endpoints require scope `test_write`.

Production API authorization rules:

- `GET /production/**`, `POST /production/planning`, and `POST /production/quality-evaluations` require scope
  `test_read`.
- `POST /production/recipes`, `POST /production/batches`, `PUT /production/**`, `PATCH /production/**`, and
  `DELETE /production/**` require scope `test_write`.

Inventory API authorization rules:

- `GET /stocks/**` and `GET /reservations/**` require scope `test_read`.
- `POST`, `PUT`, and `DELETE` operations on stock and reservation endpoints require scope `test_write`.

Swagger UI, OpenAPI docs, and actuator endpoints remain publicly accessible so that an access token can be provided
through Swagger UI and Prometheus can scrape service metrics.

Swagger OAuth configuration for secured services requests the following scopes:

- `openid`
- `test_read`
- `test_write`

Example authenticated request:

```bash
curl -H "Authorization: Bearer <access_token>" http://localhost:8081/vineyards
```

Example write request:

```bash
curl -X POST http://localhost:8081/vineyards \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Demo Vineyard","location":"Mikulov","areaSize":4.2,"description":"Small demo vineyard"}'
```

Access tokens can be obtained using the seminar OAuth client or through the Swagger UI `Authorize` dialog for secured
services.

## Kubernetes / OpenShift Deployment

Kubernetes/OpenShift deployment is not implemented for this milestone.
The application is intended to be built, started, and verified locally using Docker Compose.

## OpenAPI / Swagger

After starting a service, Swagger UI and raw OpenAPI documentation are available at:

### Customer-service

- Swagger UI: [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8084/v3/api-docs](http://localhost:8084/v3/api-docs)

### Harvest-service

- Swagger UI: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

### Inventory-service

- Swagger
  UI: [http://localhost:8083/inventory-service/api/swagger-ui.html](http://localhost:8083/inventory-service/api/swagger-ui.html)
- OpenAPI
  JSON: [http://localhost:8083/inventory-service/api/v1/api-docs](http://localhost:8083/inventory-service/api/v1/api-docs)

### Production-service

- Swagger UI: [http://localhost:8082/api/swagger-ui.html](http://localhost:8082/api/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8082/api/v3/api-docs](http://localhost:8082/api/v3/api-docs)

### Artemis Broker

Docker Compose includes an Apache ActiveMQ Artemis broker used for asynchronous communication.

- Broker TCP endpoint: `localhost:61616`
- Artemis web console: [http://localhost:8161](http://localhost:8161)
- Default credentials: `artemis` / `artemis`

### Observability

Docker Compose also starts Prometheus and Grafana for automated metrics collection and dashboarding.
Prometheus is configured to automatically scrape metrics from all application services through their
`/actuator/prometheus` endpoints.

- Prometheus UI: [http://localhost:9090](http://localhost:9090)
- Prometheus targets: [http://localhost:9090/targets](http://localhost:9090/targets)
- Grafana UI: [http://localhost:3000](http://localhost:3000)
- Grafana credentials: `admin` / `admin`
- Preloaded Grafana dashboards: `Winery Management Overview`, `Winery Service Details`

Actuator endpoints exposed by services:

- Customer service health: [http://localhost:8084/actuator/health](http://localhost:8084/actuator/health)
- Customer service actuator metrics: [http://localhost:8084/actuator/metrics](http://localhost:8084/actuator/metrics)
- Customer service Prometheus metrics: [http://localhost:8084/actuator/prometheus](http://localhost:8084/actuator/prometheus)
- Harvest service health: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
- Harvest service actuator metrics: [http://localhost:8081/actuator/metrics](http://localhost:8081/actuator/metrics)
- Harvest service Prometheus metrics: [http://localhost:8081/actuator/prometheus](http://localhost:8081/actuator/prometheus)
- Production service health: [http://localhost:8082/api/actuator/health](http://localhost:8082/api/actuator/health)
- Production service actuator metrics: [http://localhost:8082/api/actuator/metrics](http://localhost:8082/api/actuator/metrics)
- Production service Prometheus metrics: [http://localhost:8082/api/actuator/prometheus](http://localhost:8082/api/actuator/prometheus)
- Inventory service health: [http://localhost:8083/inventory-service/api/actuator/health](http://localhost:8083/inventory-service/api/actuator/health)
- Inventory service actuator metrics: [http://localhost:8083/inventory-service/api/actuator/metrics](http://localhost:8083/inventory-service/api/actuator/metrics)
- Inventory service Prometheus metrics: [http://localhost:8083/inventory-service/api/actuator/prometheus](http://localhost:8083/inventory-service/api/actuator/prometheus)

The Prometheus scrape configuration is stored in `monitoring/prometheus.yml`. Grafana provisioning files are stored under
`monitoring/grafana/provisioning`, and the preloaded dashboard is stored under `monitoring/grafana/dashboards`.

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

GenAI tools were used as an implementation support aid (code review assistance, refactoring suggestions,
README generation and updates, and documentation drafting).
All generated suggestions were manually reviewed and adjusted by team members before integration.
