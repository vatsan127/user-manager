package com.github.user_manager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @OneToOne
    @JoinColumn(
            name = "profile_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_USER_TO_PROFILE")
    )
    private UserProfile userProfile;

}
