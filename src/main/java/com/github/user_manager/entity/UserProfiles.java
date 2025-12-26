package com.github.user_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 25)
    private String unit;

    @Column(nullable = false, length = 25)
    private String team;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Timestamp createdAt;

    @JsonIgnore
    @OneToOne(mappedBy = "userProfiles")
    private Users user;
}