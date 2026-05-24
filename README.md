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

`customer-service` uses Liquibase for schema creation. By default it starts with an empty schema so tests and local
development runs are deterministic.

Run `customer-service` with realistic seed data:

```bash
mvn -pl customer-service -Dspring-boot.run.profiles=seed spring-boot:run
```

PowerShell equivalent:

```powershell
mvn -pl customer-service -D"spring-boot.run.profiles"=seed spring-boot:run
```

The seed data is stored in the Liquibase `seed` context and includes customer profiles with embedded addresses,
realistic reviews, and purchase records. Wine IDs referenced in reviews and purchases match the `wine_recipes` IDs in
`production-service`, which keeps the cross-service demo data consistent. The seed changeset also contains rollback
delete statements, so the seeded data can be cleared by rolling back that Liquibase changeset.

### Inventory-service Database Seed

`inventory-service` uses Liquibase for schema creation. By default it starts with an empty schema so tests and local
development runs are deterministic.

Run `inventory-service` with realistic seed data:

```bash
mvn -pl inventory-service -Dspring-boot.run.profiles=seed spring-boot:run
```

PowerShell equivalent:

```powershell
mvn -pl inventory-service -D"spring-boot.run.profiles"=seed spring-boot:run
```

The seed data is stored in the Liquibase `seed` context and includes realistic stock records and reservations.
Stock entries reference the same wine IDs as `production-service` `wine_recipes`, keeping the cross-service demo data
consistent end-to-end. The seed changeset also contains rollback delete statements, so the seeded data can be cleared
by rolling back that Liquibase changeset.

## Docker

Build and run services with Docker Compose:

```bash
docker compose up --build
```

In Docker Compose, `customer-service`, `harvest-service`, and `inventory-service` start with the `seed` Spring profile
by default (Liquibase loads realistic demo data), while `production-service` uses the `seed` profile only when
`PRODUCTION_SERVICE_PROFILES=seed` is set. Each service profile can be overridden through its environment variable
(`CUSTOMER_SERVICE_PROFILES`, `HARVEST_SERVICE_PROFILES`, `INVENTORY_SERVICE_PROFILES`, `PRODUCTION_SERVICE_PROFILES`) —
for example set it to an empty string to start without seed data.

Docker Compose also starts a central Swagger UI on port `8090`, so Swagger OAuth authorization can use the registered
redirect URL `http://localhost:8090/swagger-ui/oauth2-redirect.html`.

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
docker build -f harvest-service/Dockerfile -t winery-management/harvest-service:1.0.0 .
```

Run the harvest-service container:

```bash
docker run --rm -p 8081:8081 winery-management/harvest-service:1.0.0
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

The Docker Compose default already enables the `seed` profile for `harvest-service`. Set
`HARVEST_SERVICE_PROFILES` to another value if a different profile setup is needed.

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
docker build -f production-service/Dockerfile -t winery-management/production-service:1.0.0 .
```

Run the production-service container:

```bash
docker run --rm -p 8082:8082 winery-management/production-service:1.0.0
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
docker build -f customer-service/Dockerfile -t winery-management/customer-service:1.0.0 .
```

Run the customer-service container:

```bash
docker run --rm -p 8084:8084 winery-management/customer-service:1.0.0
```

## Asynchronous Communication

Implemented end-to-end message flow with ACK:

1. `production-service` (producer) sends `BatchCompletedMessage` to queue `batch-completed-queue` when a batch transitions
   to `COMPLETED`.
2. `inventory-service` (consumer) reads the message asynchronously, persists it in its inbound log, and updates the
   corresponding wine stock.
3. `inventory-service` (producer) sends a `StockUpdatedMessage` ACK to queue `stock-updated-queue`, carrying the same
   `JMSCorrelationID` as the original batch message.
4. `production-service` (consumer) reads the ACK, validates that the batch ID and wine ID match, and only then persists
   the ACK in its own inbound log. Invalid or missing ACKs throw an `IllegalStateException` and the ACK is **not**
   persisted, which keeps the inbound log clean of garbage entries.

Queue names are centralized in `common-service` (`Queues.BATCH_COMPLETED`, `Queues.STOCK_UPDATED`):

- `batch-completed-queue`
- `stock-updated-queue`

Message persistence is symmetric on both sides:

| Service | Outbound log table | Inbound log table |
|---|---|---|
| `production-service` | `OUTBOUND_MESSAGE_LOG` (BatchCompletedMessage) | `INBOUND_MESSAGE_LOG` (StockUpdatedMessage ACK) |
| `inventory-service` | `OUTBOUND_MESSAGE_LOG` (StockUpdatedMessage ACK) | `INBOUND_MESSAGE_LOG` (BatchCompletedMessage) |

This gives a full audit trail of every message that crossed the broker, on both the sending and the receiving side.

How to verify messaging end-to-end:

1. Start the stack with Docker Compose (`docker compose up --build`).
2. Create or update a production batch and set its status to `COMPLETED` through the `production-service` API
   (Swagger UI on port `8082` or via `scripts/showcase.ps1`).
3. Check Artemis console queue metrics (message count / delivering count) at http://localhost:8161.
4. Verify the four message log tables:
   - `production-service` `OUTBOUND_MESSAGE_LOG` — one row for the sent `BatchCompletedMessage`.
   - `inventory-service` `INBOUND_MESSAGE_LOG` — one row for the received `BatchCompletedMessage`.
   - `inventory-service` `OUTBOUND_MESSAGE_LOG` — one row for the sent `StockUpdatedMessage` ACK.
   - `production-service` `INBOUND_MESSAGE_LOG` — one row for the received and validated ACK.
5. Verify in `inventory-service` `GET /stocks` that the stock for the produced wine has increased by the batch's bottle
   count.

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

4. `docker_publish`
   Publishes Docker images for all executable services to Quay on pushes to the `main` branch:

- `quay.io/${QUAY_NAMESPACE}/customer-service:${CI_COMMIT_SHORT_SHA}`
- `quay.io/${QUAY_NAMESPACE}/harvest-service:${CI_COMMIT_SHORT_SHA}`
- `quay.io/${QUAY_NAMESPACE}/inventory-service:${CI_COMMIT_SHORT_SHA}`
- `quay.io/${QUAY_NAMESPACE}/production-service:${CI_COMMIT_SHORT_SHA}`

Each image is also tagged as `latest`. The publish jobs use the GitLab CI/CD variables `QUAY_NAMESPACE`,
`QUAY_USERNAME`, and `QUAY_PASSWORD`.

5. `deploy`
   Deploys the newly published commit image tag to OpenShift on pushes to the `main` branch. The deployment job logs in
   with the GitLab CI/CD variables `OPENSHIFT_SERVER`, `OPENSHIFT_TOKEN`, and `OPENSHIFT_PROJECT`, applies the OpenShift
   template, and waits until all deployments finish rolling out.

In short, the CI pipeline checks that every service:

- passes its Maven tests,
- can be built together with required module dependencies,
- has a Dockerfile that can be built successfully,
- is published to Quay from `main`,
- is deployed to OpenShift from `main`.

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

The showcase section below contains a concrete authorization-code flow for obtaining an access token with the required
`test_read` and `test_write` scopes.

## Showcase Scenario

The runnable showcase scenario demonstrates the flow from recorded harvest data to completed production and updated
inventory stock.

Scenario goal:

- read realistic harvest seed data,
- read a seeded production batch,
- mark the production batch as `COMPLETED`,
- let `production-service` publish a JMS `BatchCompletedMessage`,
- let `inventory-service` consume the message and update stock,
- verify that inventory stock increased for the produced wine.

Start the system with production seed data enabled:

```powershell
$env:PRODUCTION_SERVICE_PROFILES = "seed"
docker compose up --build
```

Get an access token with scopes `test_read` and `test_write`:

1. In PowerShell, generate the authorization URL:

```powershell
$clientId = "7e02a0a9-446a-412d-ad2b-90add47b0fdd"
$clientSecret = "48a2b2e3-4b2b-471e-b7b7-b81a85b6eeef22f347f2-3fc9-4e16-8698-3e2492701a89"
$redirectUri = "http://localhost:8090/swagger-ui/oauth2-redirect.html"
$scope = "openid test_read test_write"
$authorizeUrl = "https://id.muni.cz/oidc/authorize?response_type=code&client_id=$clientId&redirect_uri=$([uri]::EscapeDataString($redirectUri))&scope=$([uri]::EscapeDataString($scope))&state=showcase&nonce=showcase"
$authorizeUrl
```

2. Open the printed URL in a browser, sign in, and after redirect copy the `code` query parameter from the browser URL.
   The redirect page does not need to load successfully; the important part is the `code=...` value in the URL.

3. Exchange the authorization code for an access token:

```powershell
$code = "<code_from_redirect_url>"
$basicAuth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${clientId}:${clientSecret}"))
$tokenResponse = Invoke-RestMethod `
  -Method Post `
  -Uri "https://id.muni.cz/oidc/token" `
  -Headers @{ Authorization = "Basic $basicAuth" } `
  -ContentType "application/x-www-form-urlencoded" `
  -Body @{
    grant_type = "authorization_code"
    code = $code
    redirect_uri = $redirectUri
  }
$accessToken = $tokenResponse.access_token
```

Then run the showcase script from the project root:

```powershell
.\scripts\showcase.ps1 -AccessToken $accessToken
```

The script uses the seeded production batch `cccccccc-cccc-4ccc-8ccc-cccccccccccc`, which produces wine
`33333333-3333-4333-8333-333333333333`. The expected result is that the batch status changes to `COMPLETED` and the
inventory stock for that wine increases by the completed batch bottle count.

## Kubernetes / OpenShift Deployment

The project contains an OpenShift template in [
`openshift/winery-stack-template.yaml`](openshift/winery-stack-template.yaml).
It deploys:

- Apache ActiveMQ Artemis as an internal broker,
- `customer-service`,
- `harvest-service`,
- `inventory-service`,
- `production-service`,
- Prometheus,
- Grafana.

All API services, Prometheus, and Grafana get OpenShift `Route` objects so they can be tested from outside the cluster.
Artemis is exposed only inside the OpenShift project and is used by `production-service` and `inventory-service` through
`tcp://artemis:61616`. Prometheus scrapes the application services through their `/actuator/prometheus` endpoints, and
Grafana is provisioned with the Prometheus datasource and preloaded Winery Management dashboards.

### Quay Setup

Create or use an existing Quay namespace, for example your faculty login. In that namespace, create these public
repositories:

- `customer-service`
- `harvest-service`
- `inventory-service`
- `production-service`

Create a Quay robot account or token with write access to these repositories.

In GitLab, configure these project CI/CD variables:

- `QUAY_NAMESPACE`: your Quay namespace, for example `xlogin`
- `QUAY_USERNAME`: Quay username or robot account name
- `QUAY_PASSWORD`: Quay password or robot token
- `OPENSHIFT_SERVER`: OpenShift API server URL from the `oc login` command
- `OPENSHIFT_TOKEN`: OpenShift token from the `oc login` command
- `OPENSHIFT_PROJECT`: OpenShift project/namespace, for example `xlogin-dev`

Mark `QUAY_USERNAME`, `QUAY_PASSWORD`, and `OPENSHIFT_TOKEN` as masked. If the `main` branch is protected, mark the
variables as protected too.

After merging to `main`, wait for the `docker_publish` jobs and the `deploy_openshift` job to finish. The publish jobs
push each service image with both the commit tag and the `latest` tag. The deployment job uses the commit tag
`${CI_COMMIT_SHORT_SHA}` so every main-branch deployment is tied to an exact Git commit.

### OpenShift Setup

Create or open your Red Hat Developer Sandbox. In the OpenShift console, use `Copy login command`, display the token,
and copy these values into GitLab CI/CD variables:

- server URL into `OPENSHIFT_SERVER`,
- token into `OPENSHIFT_TOKEN`,
- project name into `OPENSHIFT_PROJECT`.

After this setup, deployment is automatic from the GitLab `main` pipeline. The job runs the equivalent of:

```bash
oc process -f openshift/winery-stack-template.yaml \
  -p QUAY_NAMESPACE=<your-quay-namespace> \
  -p IMAGE_TAG=<commit-short-sha> \
  | oc apply -f -
```

For local troubleshooting, log in with `oc` and run the same command manually:

```bash
oc login --token=<your-token> --server=<your-openshift-api-url>
oc project <your-openshift-project>
oc create configmap prometheus-config --from-file=prometheus.yml=monitoring/prometheus.yml --dry-run=client -o yaml | oc apply -f -
oc create configmap grafana-datasources --from-file=prometheus.yml=monitoring/grafana/provisioning/datasources/prometheus.yml --dry-run=client -o yaml | oc apply -f -
oc create configmap grafana-dashboard-providers --from-file=dashboards.yml=monitoring/grafana/provisioning/dashboards/dashboards.yml --dry-run=client -o yaml | oc apply -f -
oc create configmap grafana-dashboards --from-file=monitoring/grafana/dashboards --dry-run=client -o yaml | oc apply -f -
oc label configmap prometheus-config grafana-datasources grafana-dashboard-providers grafana-dashboards app.kubernetes.io/part-of=winery-management-system app.kubernetes.io/component=monitoring --overwrite
oc process -f openshift/winery-stack-template.yaml \
  -p QUAY_NAMESPACE=<your-quay-namespace> \
  -p IMAGE_TAG=<commit-short-sha> \
  | oc apply -f -
```

Check that all deployments become ready:

```bash
oc get deployments
oc get pods
```

Get the public API URLs:

```bash
oc get routes
```

### Current OpenShift Routes

The application is currently deployed in the course OpenShift Sandbox. The service root URLs do not serve a landing
page;
use the Swagger UI or OpenAPI links below for inspection.

Swagger UI:

- Customer
  service: [https://customer-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/swagger-ui/index.html](https://customer-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/swagger-ui/index.html)
- Harvest
  service: [https://harvest-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/swagger-ui/index.html](https://harvest-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/swagger-ui/index.html)
- Inventory
  service: [https://inventory-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/inventory-service/api/swagger-ui/index.html](https://inventory-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/inventory-service/api/swagger-ui/index.html)
- Production
  service: [https://production-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/api/swagger-ui/index.html](https://production-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/api/swagger-ui/index.html)

OpenAPI JSON:

- Customer
  service: [https://customer-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/v3/api-docs](https://customer-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/v3/api-docs)
- Harvest
  service: [https://harvest-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/v3/api-docs](https://harvest-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/v3/api-docs)
- Inventory
  service: [https://inventory-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/inventory-service/api/v1/api-docs](https://inventory-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/inventory-service/api/v1/api-docs)
- Production
  service: [https://production-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/api/v3/api-docs](https://production-service-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com/api/v3/api-docs)

Monitoring:

- Prometheus: `https://prometheus-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com`
- Grafana: `https://grafana-xsokolik-dev.apps.rm1.0a51.p1.openshiftapps.com`
- Grafana credentials: `admin` / `admin`

These URLs are tied to the current OpenShift Sandbox cluster and namespace. If the Sandbox is recreated or the routes
change, redeploy the project and run `oc get routes` to get the new public hosts.

### OpenShift Smoke Checks

Use the route hostnames from `oc get routes`. The OpenShift routes use edge TLS termination, so public links are
available through `https://`.

Swagger/OpenAPI checks:

```bash
curl http://<customer-service-route>/v3/api-docs
curl http://<harvest-service-route>/v3/api-docs
curl http://<inventory-service-route>/inventory-service/api/v1/api-docs
curl http://<production-service-route>/api/v3/api-docs
curl http://<prometheus-route>/-/ready
curl http://<grafana-route>/api/health
```

Messaging connection check:

```bash
oc logs deployment/production-service
oc logs deployment/inventory-service
```

The logs should not contain connection errors for `tcp://artemis:61616`.

### Waking Idle OpenShift Pods

The Red Hat Developer Sandbox can scale idle workloads down automatically. To wake the application without changing the
deployed image, run the manual GitLab CI job `wake_openshift` from the latest `main` pipeline. The job uses the same
`OPENSHIFT_SERVER`, `OPENSHIFT_TOKEN`, and `OPENSHIFT_PROJECT` variables as deployment, scales all deployments to one
replica, and waits for rollout completion.

Equivalent manual commands:

```bash
oc scale deployment/artemis --replicas=1
oc scale deployment/customer-service --replicas=1
oc scale deployment/harvest-service --replicas=1
oc scale deployment/inventory-service --replicas=1
oc scale deployment/production-service --replicas=1
oc scale deployment/prometheus --replicas=1
oc scale deployment/grafana --replicas=1
```

Wait until the pods are available:

```bash
oc rollout status deployment/artemis
oc rollout status deployment/customer-service
oc rollout status deployment/harvest-service
oc rollout status deployment/inventory-service
oc rollout status deployment/production-service
oc rollout status deployment/prometheus
oc rollout status deployment/grafana
```

### Scaling And Cleanup

Scale an API service, for example `harvest-service`, to two pods:

```bash
oc scale deployment/harvest-service --replicas=2
```

Scale it back down:

```bash
oc scale deployment/harvest-service --replicas=1
```

Delete all Winery Management System OpenShift resources:

```bash
oc delete all,route,configmap -l app.kubernetes.io/part-of=winery-management-system
```

## OpenAPI / Swagger

After starting a service, Swagger UI and raw OpenAPI documentation are available at:

Central Swagger UI with OAuth authorize flow for all services:
[http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)

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

- Broker TCP endpoint: `localhost:61690`
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

Tools used: **Claude** and **ChatGPT**, as in-IDE assistants. Every AI suggestion was reviewed and committed by a human; no AI had autonomous push/merge access.

Where AI helped:

- Documentation (README, OpenAPI descriptions, `CLAUDE.md`, milestone instructions, EN/SK translations).
- Scaffolding (Liquibase changesets, OpenAPI annotations, JMS producer/consumer skeleton, `common-service` extraction).
- Infrastructure (GitLab CI pipeline, OpenShift template, Prometheus/Grafana provisioning).
- Debugging (CHECKLIST audit against the codebase, multi-layer customer-service Docker Compose fix).
- Defense prep (slide deck, demo scenario, draft answers for tutor Q&A).
- Code review as a second pair of eyes (edge cases, naming consistency, refactor candidates).

Not delegated to AI: business logic, security policy decisions, messaging contract design, final code review and commits.
