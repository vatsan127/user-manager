# JPA One-to-One Mapping Notes

## Project Setup

```bash
# Create the database
psql -d postgres

create database user_db;

\q
```

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

## Overview

A one-to-one relationship means that one entity is associated with exactly one instance of another entity.

---

## Key Concepts

### Bidirectional vs Unidirectional
- **Unidirectional**: Only one entity knows about the relationship
- **Bidirectional**: Both entities know about each other

### Owning Side vs Inverse Side
- **Owning Side**: The entity that contains the foreign key in the database
- In unidirectional relationships, there's only one side (the owning side)
- **Inverse Side**: The entity that references the owning side using `mappedBy`
- The inverse side concept exists ONLY in bidirectional relationships!

---

## Important Annotations

### @Column
- Used to specify constraints and custom name
- If name is not specified with `@Column`, Hibernate will follow snake_case
- Column definitions can be given with SQL

### @OneToOne
Marks a one-to-one relationship

**Attributes:**
- `cascade`: Defines cascade operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH, ALL)
- `fetch`: Lazy or Eager loading (default is EAGER for @OneToOne)
- `optional`: Whether the relationship is optional (default is true)
- `mappedBy`: Used on the inverse side to specify the owning side
- `orphanRemoval`: Whether to remove orphaned entities (default is false)

**Orphan Removal Explained**: An "orphan" is a child entity that's no longer referenced by its parent. If `orphanRemoval = true`, when you do `user.setProfile(null)` or `user.removeProfile()`, the orphaned UserProfile will be automatically deleted from the database. Without it, the profile remains in DB.

### @JoinColumn
Specifies the foreign key column

**Attributes:**
- `name`: Name of the foreign key column
- `referencedColumnName`: Column in the target entity (usually primary key)
- `nullable`: Whether the foreign key can be null
- `unique`: Whether the foreign key should be unique
- `foreignKey`: Foreign Key configurations

**What if @JoinColumn is not specified?**
- JPA will create a default foreign key column name
- Default naming: `{property_name}_{referenced_column_name}`
- Example: If property is "profile" and User's PK is "id", the column will be named "profile_id"
- It's better to explicitly specify @JoinColumn for clarity!

### @ForeignKey
Used within `@JoinColumn` to specify a custom foreign key constraint name

**Attributes:**
- `name`: Name of the foreign key constraint

**Example:**
```java
@JoinColumn(
    name = "profile_id",
    referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "FK_USER_TO_PROFILE")
)
```

**Why use it?**
- Without this, JPA generates random constraint names like `FKa1b2c3d4e5`
- Meaningful names help with debugging and database administration
- Easier to identify constraint violations in error messages

### @CreationTimestamp (Hibernate)
Hibernate-specific annotation for automatic timestamp generation on entity creation

**Example:**
```java
@CreationTimestamp
@Column(name = "created_at", nullable = false, updatable = false)
private Timestamp createdAt;
```

**Key Points:**
- Automatically sets the field value when the entity is first persisted
- Use `updatable = false` in `@Column` to prevent modification after creation
- **Related**: `@UpdateTimestamp` for tracking last modification time

---

## Cascade Types

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

---

## Fetch Types

- **FetchType.LAZY**: Related entity loaded on-demand (recommended)
- **FetchType.EAGER**: Related entity loaded immediately (default for @OneToOne)

---

## Key Implementation Points

1. Users entity has `@OneToOne` with `@JoinColumn` (owning side)
2. UserProfiles entity has `@OneToOne` with `mappedBy` (inverse side) - **bidirectional relationship**
3. Using `CascadeType.ALL` means saving a User will also save its UserProfile
4. Using `orphanRemoval = true` ensures removing profile from user deletes it from DB
5. Default `FetchType.EAGER` is used for @OneToOne; use `FetchType.LAZY` for better performance

---

## Common Pitfalls

### N+1 Query Problem

**Why "N+1"?** Imagine fetching 100 users:
- 1 query to get all users: `SELECT * FROM users` (this is the "1")
- Then N queries (100 queries) to get each user's profile if EAGER:
  - `SELECT * FROM user_profiles WHERE id = ?` (profile_id from user 1)
  - `SELECT * FROM user_profiles WHERE id = ?` (profile_id from user 2)
  - ... (100 times - this is the "N")
- **Total = 1 + 100 = 101 queries!** This is the N+1 problem

**Issue**: Using EAGER fetch causes this problem when loading multiple entities

**Solution**: Use LAZY fetch and JOIN FETCH when you actually need the profile
```sql
SELECT u FROM Users u LEFT JOIN FETCH u.userProfiles
```
This executes only 1 query instead of 101!

### Bidirectional Relationship Management
- **Issue**: Not setting both sides of the relationship
- **Solution**: Use convenience methods to set both sides

```java
// Convenience method in Users entity
public void setUserProfile(UserProfiles profile) {
    this.userProfiles = profile;
    if (profile != null) {
        profile.setUser(this);
    }
}
```

### Cascade Operations
- **Issue**: Accidentally deleting related entities
- **Solution**: Be careful with `CascadeType.REMOVE` and `orphanRemoval`

### Unique Constraint
- **Issue**: Forgetting to make foreign key unique
- **Solution**: Add `unique = true` in `@JoinColumn`

### Infinite Recursion in JSON Serialization
- **Issue**: In bidirectional relationships, serializing to JSON causes infinite loop (User → Profile → User → ...)
- **Solutions**: Multiple approaches available:

**Option 1: @JsonIgnore** (Simplest - used in this project)
```java
@JsonIgnore
@OneToOne(mappedBy = "userProfiles")
private Users user;
```
- Completely excludes the field from serialization and deserialization
- Best when you never need the back-reference in JSON

**Option 2: @JsonManagedReference / @JsonBackReference**
```java
// Owner side (User) - serialized normally
@JsonManagedReference
@OneToOne
@JoinColumn(name = "profile_id")
private UserProfile profile;

// Inverse side (UserProfile) - excluded during serialization
@JsonBackReference
@OneToOne(mappedBy = "profile")
private User user;
```
- `@JsonManagedReference`: Forward part, serialized normally
- `@JsonBackReference`: Back part, omitted from serialization
- Maintains the relationship during deserialization

**Option 3: @JsonIdentityInfo**
```java
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class User { ... }
```
- Uses object identity to handle circular references
- First occurrence serializes full object, subsequent occurrences serialize only the ID
- Useful when both sides need to be serialized

---

## Database Schema

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
    CONSTRAINT FK_USER_TO_PROFILE FOREIGN KEY (profile_id) REFERENCES user_profiles(id),
    UNIQUE (profile_id) -- Unique constraint for One-to-One relationship
);
```

---

## Sample Data

```sql
INSERT INTO user_profiles (phone_number, team, unit)
VALUES ('9876543210', 'marketing', 'customer value');

INSERT INTO users (first_name, last_name, profile_id)
VALUES ('srivatsan', 'n', 1);
```
