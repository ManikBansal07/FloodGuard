package com.floodguard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "flood_predictions")
public class FloodPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Location location;

    private Double riskLevel; // 0.0 to 1.0

    @Enumerated(EnumType.STRING)
    private RiskCategory riskCategory;

    private LocalDateTime predictionTime;

    private LocalDateTime validUntil;

    private Double estimatedPropertyDamage;

    private Double estimatedBusinessInterruption;

    private Double estimatedInsurancePayout;

    private String weatherData; // JSON string of weather conditions

    private String modelVersion;
} 