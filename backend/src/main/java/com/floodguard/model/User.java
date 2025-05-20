package com.floodguard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private String phoneNumber;

    @ElementCollection
    @CollectionTable(name = "user_saved_locations")
    private Set<Location> savedLocations = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private boolean enabled = true;

    // Test user credentials
    public static final String TEST_EMAIL = "test@floodguard.com";
    public static final String TEST_PASSWORD = "Test@123";
} 