# Open Session in View (OSIV) Demo

Demo application illustrating the Open Session in View (OSIV) anti-pattern in Spring Boot. This project demonstrates the
difference between relying on OSIV for lazy loading versus using explicit fetch joins.

## What is OSIV?

Open Session in View is a pattern where the Hibernate session (JPA EntityManager) remains open during the entire HTTP
request, including view rendering. While this prevents `LazyInitializationException`, it can lead to:

- **N+1 query problems**: Lazy associations trigger additional database queries during JSON serialization
- **Long database connections**: The session stays open longer than necessary
- **Hidden performance issues**: Problems only manifest under load
- **Tight coupling**: View layer becomes dependent on the persistence layer

Spring Boot enables OSIV by default and shows this warning:

```
spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering.
```

## Domain Model

```
Customer (1) ──── (*) PurchaseOrder (1) ──── (*) OrderItem (*) ──── (1) Product
```

- `Customer` → `PurchaseOrder`: OneToMany, bidirectional
- `PurchaseOrder` → `OrderItem`: OneToMany, unidirectional via @JoinColumn
- `OrderItem` → `Product`: ManyToOne, LAZY fetch

## API Endpoints

### GET /orders/osiv

Uses standard `findAll()` and relies on OSIV for lazy loading. With OSIV enabled, this works but triggers multiple
additional queries during JSON serialization (N+1 problem).

### GET /orders/fetch

Uses `findAllFetchRelations()` with explicit JOIN FETCH. This loads all data in a single query, regardless of whether
OSIV is enabled.

```java

@Query("select o from PurchaseOrder o join fetch o.items i join fetch o.customer c join fetch i.product")
List<PurchaseOrder> findAllFetchRelations();
```

## Running the Application

### Prerequisites

- Java 25
- Docker (for Testcontainers)

### Start with Testcontainers (recommended)

```bash
./mvnw spring-boot:test-run
```

This starts the application with an auto-provisioned PostgreSQL container.

### Test the Endpoints

```bash
# With OSIV (multiple queries)
curl http://localhost:8080/orders/osiv

# With explicit fetch (single query)
curl http://localhost:8080/orders/fetch
```

Or use the provided `requests.http` file in your IDE.

## Build Commands

```bash
./mvnw compile              # Compile
./mvnw test                 # Run tests
./mvnw spring-boot:run      # Run (requires external PostgreSQL)
```

## Tech Stack

- Spring Boot 4.0.1
- Java 25
- Spring Data JPA
- PostgreSQL
- Flyway (database migrations)
- Testcontainers
- ModelMapper

## Recommendation

Disable OSIV in production applications:

```properties
spring.jpa.open-in-view=false
```

Then use explicit fetch joins or DTOs with projections to load exactly the data you need.
