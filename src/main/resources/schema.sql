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