# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Demo application illustrating the Open Session in View (OSIV) anti-pattern in Spring Boot. Compares lazy loading behavior with OSIV enabled vs explicit fetch joins.

## Build Commands

```bash
./mvnw compile              # Compile
./mvnw test                 # Run tests
./mvnw spring-boot:run      # Run application (requires PostgreSQL)
./mvnw test -Dtest=ClassName#methodName  # Run single test
```

## Development with Testcontainers

Run the application locally with an auto-provisioned PostgreSQL container:
```bash
./mvnw spring-boot:test-run
```
This uses `TestOrderOsivApplication` which configures Testcontainers automatically.

## Architecture

### Domain Model (JPA Entities)
- `Customer` → `PurchaseOrder` (OneToMany, bidirectional)
- `PurchaseOrder` → `OrderItem` (OneToMany, unidirectional via @JoinColumn)
- `OrderItem` → `Product` (ManyToOne, LAZY fetch)

### API Endpoints
Two endpoints demonstrate OSIV behavior:
- `GET /orders/osiv` - Uses standard `findAll()`, relies on OSIV for lazy loading
- `GET /orders/fetch` - Uses `findAllFetchRelations()` with explicit JOIN FETCH

### Key Files
- `PurchaseOrderRepository` - Contains the fetch join query demonstrating proper eager loading
- `OrderController` - Uses ModelMapper with LOOSE matching strategy for entity→DTO conversion

## Tech Stack
- Spring Boot 4.0.1, Java 25
- Spring Data JPA, PostgreSQL
- Flyway migrations (schema in `src/main/resources/db/migration/`, test data in `src/test/resources/db/migration/`)
- Testcontainers for integration testing
- ModelMapper for DTO mapping