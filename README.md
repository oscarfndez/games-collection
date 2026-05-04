# Inventory Service

Spring Boot microservice responsible for the game inventory, platforms, studios, and user game collections in Games Collection.

![Games Collection Architecture](docs/architecture.png)

## What It Does

- Manages games through CRUD endpoints.
- Manages platforms through CRUD endpoints.
- Manages studios through CRUD endpoints.
- Manages each user's game collection.
- Supports a many-to-many relationship between games and platforms.
- Supports an optional studio association for games.
- Consumes user lifecycle events from ActiveMQ.
- Deactivates a user's collection items when a user deletion event is received.
- Provides OpenAPI/Swagger documentation.
- Exposes Prometheus metrics through Spring Boot Actuator.

## Tech Stack

- Java 17
- Spring Boot 3.4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- ActiveMQ
- Springdoc OpenAPI
- Micrometer + Prometheus
- JUnit 5, Mockito, Testcontainers
- Maven
- Jib for Docker image publishing

## Requirements

- Java 17 or newer
- Maven 3.9 or the included Maven wrapper
- PostgreSQL 16, either local or through `kubectl port-forward`
- ActiveMQ, either local or through `kubectl port-forward`
- Docker, only for integration tests based on Testcontainers

## Clone

```bash
git clone <inventory-service-repository-url>
cd inventory-service
```

## Local Configuration

Default local configuration expects:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
```

Useful environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/inventory
SPRING_DATASOURCE_USERNAME=oscar
SPRING_DATASOURCE_PASSWORD=password
SPRING_ACTIVEMQ_BROKER_URL=tcp://localhost:61616
SPRING_ACTIVEMQ_USER=admin
SPRING_ACTIVEMQ_PASSWORD=admin
USER_EVENTS_TOPIC=games-collection.user-events
USER_EVENTS_SUBSCRIPTION=inventory-service-user-events
JMS_CLIENT_ID=inventory-service
JMS_LISTENER_AUTO_STARTUP=true
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_APP=INFO
LOGGING_LEVEL_SECURITY=WARN
```

To use the Kubernetes services from your local machine:

```powershell
kubectl -n dev port-forward svc/postgres 5431:5432
kubectl -n dev port-forward svc/activemq 61616:61616
```

PowerShell example:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5431/inventory"
$env:SPRING_DATASOURCE_USERNAME="oscar"
$env:SPRING_DATASOURCE_PASSWORD="password"
$env:SPRING_ACTIVEMQ_BROKER_URL="tcp://localhost:61616"
.\mvnw.cmd spring-boot:run
```

## Run

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Tests

Unit tests:

```bash
./mvnw test
```

Full build without integration tests:

```bash
./mvnw clean verify
```

Integration tests with Testcontainers:

```bash
./mvnw verify -Pintegration-tests
```

Integration tests require Docker.

## Main Endpoints

Games:

```http
GET    /api/game
GET    /api/game/all?page=0&size=10&sortField=name&sortDir=asc
POST   /api/game
PUT    /api/game?id={uuid}
DELETE /api/game?id={uuid}
```

Platforms:

```http
GET    /api/platform
GET    /api/platform/all?page=0&size=10&sortField=name&sortDir=asc
POST   /api/platform
PUT    /api/platform?id={uuid}
DELETE /api/platform?id={uuid}
```

Studios:

```http
GET    /api/studio
GET    /api/studio/all?page=0&size=10&sortField=name&sortDir=asc
POST   /api/studio
PUT    /api/studio?id={uuid}
DELETE /api/studio?id={uuid}
```

User collection:

```http
GET    /api/collection
POST   /api/collection
PUT    /api/collection?id={uuid}
DELETE /api/collection?id={uuid}
```

Security is JWT-based. Administrative inventory endpoints require `ADMIN`; personal collection endpoints require an authenticated user.

## Observability

Actuator:

```http
GET /actuator/health
GET /actuator/prometheus
```

Swagger:

```http
GET /swagger-ui.html
GET /v3/api-docs
```

In the cluster, Swagger is available at:

```text
http://oscarfndez.eu/gamescollection/docs/inventory/swagger-ui.html
```

## Consumed Events

This service consumes JSON events from ActiveMQ:

```text
topic: games-collection.user-events
subscription: inventory-service-user-events
```

The `afterDeleting` user event marks that user's collection items as inactive.

## Scripts

The `scripts/` directory contains operational utilities related to data and maintenance. Review each script before running it against a real database.

## CI/CD

The `Jenkinsfile` runs:

- Checkout.
- `mvn clean verify`.
- SonarCloud analysis.
- Docker image publishing to Docker Hub with Jib.

Image:

```text
oscarfndez/inventory-service:build-<BUILD_NUMBER>
```

Kubernetes deployment is managed from the `fleet-infra` repository through Flux GitOps.
