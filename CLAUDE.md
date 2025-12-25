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
- `entity/` - JPA entities with one-to-one relationship
- `repository/` - Spring Data JPA repositories
- `service/` - Business logic layer
- `controller/` - REST API endpoints

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

## API Documentation

**Swagger UI**: http://localhost:8080/user-manager/v1/swagger-ui.html
**OpenAPI Spec**: http://localhost:8080/user-manager/v1/api-docs

### Endpoints

- `GET /user-manager/v1/users` - Retrieve all users with their profiles

## Important Notes

- The application uses constructor-based dependency injection (no `@Autowired`)
- JPA configuration has `open-in-view: false` to prevent lazy loading issues
- HikariCP connection pool configured with 30 max connections, 15 minimum idle
- Database schema defined in `src/main/resources/OneToOne-notes.txt:130-146`
- Detailed JPA one-to-one mapping concepts documented in `src/main/resources/OneToOne-notes.txt`
