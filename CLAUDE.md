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
- Database: `user_db`
- Schema: `public`
- Username: `srivatsan`
- Password: `password`

## Architecture

### Package Structure
- `api/` - API interface contracts with OpenAPI/Swagger documentation
- `aspect/` - AOP aspects for cross-cutting concerns (logging)
- `config/` - Application configuration classes
- `controller/` - REST API endpoint implementations
- `entity/` - JPA entities with one-to-one relationship
- `repository/` - Spring Data JPA repositories
- `service/` - Business logic layer

### One-to-One Relationship Implementation

The core architecture demonstrates a **bidirectional one-to-one** relationship:

**Users (Owning Side)**
- Contains the foreign key (`profile_id`) to UserProfiles
- Uses `@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)` with `@JoinColumn` to define the relationship
- The foreign key constraint is named `FK_USER_TO_PROFILE`

**UserProfiles (Inverse Side)**
- Has a back-reference to Users via `@OneToOne(mappedBy = "userProfiles")`
- Can be managed independently via ProfilesController

### Key Implementation Details

1. **Foreign Key Ownership**: Users entity owns the relationship via the `profile_id` column
2. **Orphan Removal**: `orphanRemoval = true` ensures detached profiles are deleted from DB
3. **Hibernate DDL Mode**: Set to `none` - schema changes are not auto-applied
4. **SQL Logging**: Via `org.hibernate.SQL=DEBUG` in logback (not `show-sql` to avoid duplicate logging)
5. **Lombok Usage**: `@Data` annotation generates getters/setters/toString/equals/hashCode
6. **Context Path**: API is served at `/user-manager/v1` (constructed from app name and version)
7. **Timestamp Management**: `@CreationTimestamp` on UserProfiles.createdAt for automatic timestamp generation
8. **AOP Logging**: Centralized logging via `LoggingAspect` - logs method entry, exit, execution time, and exceptions
9. **API Contract Pattern**: Swagger annotations defined in `api` package interfaces, implemented by controllers

## API Documentation

**Swagger UI**: http://localhost:8080/user-manager/v1/swagger-ui.html
**OpenAPI Spec**: http://localhost:8080/user-manager/v1/api-docs

### User Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/users` | Retrieve all users with profiles | 200 OK |
| POST | `/users` | Create new user with optional profile | 201 Created |
| PUT | `/users/{id}` | Update existing user and profile | 200 OK |
| DELETE | `/users/{id}` | Delete user and associated profile | 204 No Content |

### Profile Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/profiles` | Retrieve all profiles | 200 OK |
| GET | `/profiles/{id}` | Retrieve profile by id | 200 OK |
| POST | `/profiles` | Create standalone profile | 201 Created |
| PUT | `/profiles/{id}` | Update profile | 200 OK |
| DELETE | `/profiles/{id}` | Delete profile | 204 No Content |

**Full API Path**: `http://localhost:8080/user-manager/v1{endpoint}`

## Important Notes

- The application uses constructor-based dependency injection (no `@Autowired`)
- JPA configuration has `open-in-view: false` to prevent lazy loading issues
- HikariCP connection pool configured with 10 max connections, 10 minimum idle
- JPA one-to-one mapping notes documented in `README.md`

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
   - Delegates to repositories for data access

5. **DTO Pattern** (Not Implemented):
   - Currently exposes entities directly
   - Consider DTOs for production to decouple API from database model
