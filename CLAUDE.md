# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 4.0.1 application demonstrating JPA one-to-one relationships between Users and UserProfiles entities. Uses Java 21, PostgreSQL, and Lombok.

## Build and Run Commands

```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run with specific profile
SPRING_PROFILES_ACTIVE=postgres ./mvnw spring-boot:run

# Compile only
./mvnw compile

# Run tests (when available)
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName
```

## Database Configuration

The application uses PostgreSQL with configuration in `application-postgres.yaml`:
- Host: `localhost:5432`
- Database: `postgres`
- Schema: `public`
- Username: `postgres`
- Password: `password`

## Architecture

### Package Structure
- `api/` - API interface contracts with OpenAPI/Swagger documentation
- `aspect/` - AOP aspects for cross-cutting concerns (logging)
- `config/` - Application configuration classes
- `controller/` - REST API endpoint implementations
- `entity/` - JPA entities with one-to-one relationship
- `repository/` - Spring Data JPA repositories
- `service/` - Business logic layer with transaction management

### One-to-One Relationship Implementation

The core architecture demonstrates a **unidirectional one-to-one** relationship:

**Users (Owning Side)**
- Contains the foreign key (`profile_id`) to UserProfiles
- Uses `@OneToOne` with `@JoinColumn` to define the relationship
- The foreign key constraint is named `FK_USER_TO_PROFILE`

**UserProfiles (Referenced Side)**
- Does NOT have a back-reference to Users (unidirectional)
- Standalone entity that can exist independently

This is NOT a bidirectional relationship - UserProfiles has no `mappedBy` annotation or reference back to Users.

### Key Implementation Details

1. **Foreign Key Ownership**: Users entity owns the relationship via the `profile_id` column
2. **Hibernate DDL Mode**: Set to `update` - schema changes are auto-applied
3. **SQL Visibility**: `show-sql: true` and `format_sql: true` for debugging
4. **Lombok Usage**: `@Data` annotation generates getters/setters/toString/equals/hashCode
5. **Context Path**: API is served at `/user-manager/v1` (constructed from app name and version)
6. **Timestamp Management**: `@CreationTimestamp` on UserProfiles.createdAt for automatic timestamp generation
7. **AOP Logging**: Centralized logging via `LoggingAspect` - logs method entry, exit, execution time, and exceptions
8. **API Contract Pattern**: Swagger annotations defined in `api` package interfaces, implemented by controllers
9. **Transaction Management**: `@Transactional` on service layer methods for data consistency

## API Documentation

**Swagger UI**: http://localhost:8080/user-manager/v1/swagger-ui.html
**OpenAPI Spec**: http://localhost:8080/user-manager/v1/api-docs

### Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/users` | Retrieve all users with profiles | 200 OK |
| POST | `/users` | Create new user with optional profile | 201 Created |
| PUT | `/users/{id}` | Update existing user and profile | 200 OK |
| DELETE | `/users/{id}` | Delete user and associated profile | 204 No Content |

**Full API Path**: `http://localhost:8080/user-manager/v1{endpoint}`

## Important Notes

- The application uses constructor-based dependency injection (no `@Autowired`)
- JPA configuration has `open-in-view: false` to prevent lazy loading issues
- HikariCP connection pool configured with 30 max connections, 15 minimum idle
- Database schema defined in `src/main/resources/OneToOne-notes.txt:130-146`
- Detailed JPA one-to-one mapping concepts documented in `src/main/resources/OneToOne-notes.txt`

## AOP Implementation

The application uses Spring AOP (`spring-boot-starter-aop`) for centralized logging:

**LoggingAspect** (`aspect/LoggingAspect.java`):
- Intercepts all controller and service layer methods
- `@Before`: Logs method entry with arguments
- `@Around`: Logs execution time and return values
- `@AfterThrowing`: Logs exceptions with stack traces
- Eliminates need for manual logging in business logic

**Log Format**:
```
ClassName :: methodName :: Entry :: args=[...]
ClassName :: methodName :: Exit :: executionTime=15ms :: result=...
ClassName :: methodName :: Exception :: executionTime=10ms :: error=...
```

## Design Patterns

1. **Interface-Based API Contracts**:
   - API interfaces in `api` package define Swagger documentation
   - Controllers implement interfaces for clean separation

2. **Aspect-Oriented Programming**:
   - Cross-cutting concerns (logging) handled via AOP
   - Keeps business logic clean and focused

3. **Repository Pattern**:
   - Spring Data JPA repositories abstract data access
   - No manual SQL queries needed

4. **Service Layer Pattern**:
   - Business logic encapsulated in service layer
   - Transaction boundaries defined with `@Transactional`

5. **DTO Pattern** (Not Implemented):
   - Currently exposes entities directly
   - Consider DTOs for production to decouple API from database model
