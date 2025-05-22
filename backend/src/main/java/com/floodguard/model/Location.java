package com.floodguard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double elevation;

    @Column(name = "rainfall")
    private Double rainfall;

    @Column(name = "soil_type")
    private String soilType;

    @Column(name = "drainage_system")
    private String drainageSystem;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private java.util.List<FloodReport> reports;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private java.util.List<FloodPrediction> predictions;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 