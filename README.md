# User Manager - JPA One-to-One Mapping

A Spring Boot application demonstrating JPA one-to-one relationship mapping between Users and UserProfiles entities.

**Technology Stack**: Java 21, Spring Boot 4.0.1, PostgreSQL, JPA/Hibernate, Lombok, Spring AOP

**API Documentation**: [Swagger UI](http://localhost:8080/user-manager/v1/swagger-ui.html)

## Quick Start

```bash
# Run the application
./mvnw spring-boot:run

# Access Swagger UI
http://localhost:8080/user-manager/v1/swagger-ui.html
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/user-manager/v1/users` | Get all users with profiles |
| POST | `/user-manager/v1/users` | Create new user |
| PUT | `/user-manager/v1/users/{id}` | Update user |
| DELETE | `/user-manager/v1/users/{id}` | Delete user |

All endpoints include request/response examples in Swagger UI.

---

## Entity Relationship

```
┌─────────────┐         ┌──────────────────┐
│   Users     │         │  UserProfiles    │
├─────────────┤         ├──────────────────┤
│ id (PK)     │         │ id (PK)          │
│ firstName   │         │ unit             │
│ lastName    │         │ team             │
│ profile_id  │────────>│ phoneNumber      │
│   (FK)      │ 1:1     │ createdAt        │
└─────────────┘         └──────────────────┘
```

---

## ONE-TO-ONE MAPPING IN JPA - NOTES

### OVERVIEW

A one-to-one relationship means that one entity is associated with exactly one instance of another entity.

### KEY CONCEPTS

#### 1. Bidirectional vs Unidirectional
- **Unidirectional**: Only one entity knows about the relationship
- **Bidirectional**: Both entities know about each other

#### 2. Owning Side vs Inverse Side
- **Owning Side**: The entity that contains the foreign key in the database
- In unidirectional relationships, there's only one side (the owning side)
- **Inverse Side**: The entity that references the owning side using `mappedBy`
- The inverse side concept exists ONLY in bidirectional relationships!

### IMPORTANT ANNOTATIONS

#### @Column
- Used to specify constraints and custom name
- If name is not specified with `@Column`, Hibernate will follow snake_case
- Column definitions can be given with SQL

#### @OneToOne
Marks a one-to-one relationship

**Attributes:**
- `cascade`: Defines cascade operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH, ALL)
- `fetch`: Lazy or Eager loading (default is EAGER for @OneToOne)
- `optional`: Whether the relationship is optional (default is true)
- `mappedBy`: Used on the inverse side to specify the owning side
- `orphanRemoval`: Whether to remove orphaned entities (default is false)

**EXPLANATION**: An "orphan" is a child entity that's no longer referenced by its parent. If `orphanRemoval = true`, when you do `user.setProfile(null)` or `user.removeProfile()`, the orphaned UserProfile will be automatically deleted from the database. Without it, the profile remains in DB.

#### @JoinColumn
Specifies the foreign key column

**Attributes:**
- `name`: Name of the foreign key column
- `referencedColumnName`: Column in the target entity (usually primary key)
- `nullable`: Whether the foreign key can be null
- `unique`: Whether the foreign key should be unique
- `foreignKey`: Foreign Key configurations

**WHAT IF @JoinColumn IS NOT SPECIFIED?**
- JPA will create a default foreign key column name
- Default naming: `{property_name}_{referenced_column_name}`
- Example: If property is "profile" and User's PK is "id", the column will be named "profile_id"
- It's better to explicitly specify @JoinColumn for clarity!

### CASCADE TYPES - DETAILED EXPLANATION

- **CascadeType.PERSIST**: When you save (persist) a User, its UserProfile is also automatically saved
  - Example: `em.persist(user)` → profile also persisted

- **CascadeType.MERGE**: When you merge (update) a User, its UserProfile is also automatically merged
  - Example: `em.merge(user)` → profile also merged

- **CascadeType.REMOVE**: When you delete a User, its UserProfile is also deleted
  - Example: `em.remove(user)` → profile also removed

- **CascadeType.REFRESH**: When you refresh a User from DB, its UserProfile is also refreshed
  - Example: `em.refresh(user)` → profile also refreshed

- **CascadeType.DETACH**: When you detach a User from persistence context, its UserProfile is also detached
  - Example: `em.detach(user)` → profile also detached

- **CascadeType.ALL**: All of the above operations cascade to related entities

### FETCH TYPES

- **FetchType.LAZY**: Related entity loaded on-demand (recommended)
- **FetchType.EAGER**: Related entity loaded immediately (default for @OneToOne)

### KEY IMPLEMENTATION POINTS

1. User entity has `@OneToOne` with `@JoinColumn` (owning side)
2. UserProfile entity has `@OneToOne` with `mappedBy` (inverse side)
3. Using `CascadeType.ALL` means saving a User will also save its UserProfile
4. Using `orphanRemoval = true` means removing profile from user deletes it
5. Using `FetchType.LAZY` improves performance by not loading profile unless needed

### COMMON PITFALLS

#### 1. N+1 Query Problem

**WHY "N+1"?** Imagine fetching 100 users:
- 1 query to get all users: `SELECT * FROM users` (this is the "1")
- Then N queries (100 queries) to get each user's profile if EAGER:
  - `SELECT * FROM user_profiles WHERE id = ?` (profile_id from user 1)
  - `SELECT * FROM user_profiles WHERE id = ?` (profile_id from user 2)
  - ... (100 times - this is the "N")
- **Total = 1 + 100 = 101 queries!** This is the N+1 problem

**Issue**: Using EAGER fetch causes this problem when loading multiple entities

**Solution**: Use LAZY fetch and JOIN FETCH when you actually need the profile
```sql
SELECT u FROM User u LEFT JOIN FETCH u.profile
```
This executes only 1 query instead of 101!

#### 2. Bidirectional Relationship Management
- **Issue**: Not setting both sides of the relationship
- **Solution**: Use convenience methods to set both sides

#### 3. Cascade Operations
- **Issue**: Accidentally deleting related entities
- **Solution**: Be careful with `CascadeType.REMOVE` and `orphanRemoval`

#### 4. Unique Constraint
- **Issue**: Forgetting to make foreign key unique
- **Solution**: Add `unique = true` in `@JoinColumn`

### DATABASE SCHEMA

```sql
CREATE TABLE user_profiles (
    id SERIAL PRIMARY KEY,
    unit VARCHAR(25) NOT NULL,
    team VARCHAR(25) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    profile_id INTEGER,
    FOREIGN KEY (profile_id) REFERENCES user_profiles(id),
    UNIQUE (profile_id) -- Unique constraint for One-to-One relationship
);
```

### SAMPLE DATA

```sql
INSERT INTO user_profiles (phone_number, team, unit)
VALUES ('9876543210', 'marketing', 'customer value');

INSERT INTO users (first_name, last_name, profile_id)
VALUES ('srivatsan', 'n', 1);
```
