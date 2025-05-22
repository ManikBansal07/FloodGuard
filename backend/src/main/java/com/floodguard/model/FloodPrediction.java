package com.floodguard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "flood_predictions")
public class FloodPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "risk_score", nullable = false)
    private Double riskScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_category", nullable = false)
    private RiskCategory riskCategory;

    @Column(nullable = false)
    private Double confidence;

    @ElementCollection
    @CollectionTable(name = "prediction_factors")
    @MapKeyColumn(name = "factor_name")
    @Column(name = "factor_value")
    private Map<String, Double> factors;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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