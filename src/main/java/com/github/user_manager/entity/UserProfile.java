package com.github.user_manager.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 25)
    private String unit;

    @Column(nullable = false, length = 25)
    private String team;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private Timestamp createdAt;
}