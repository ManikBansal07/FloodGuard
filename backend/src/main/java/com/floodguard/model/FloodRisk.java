package com.floodguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class FloodRisk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double latitude;
    private Double longitude;
    private Double riskScore;
    private String riskLevel;
    
    @ElementCollection
    private List<String> factors;
    
    private LocalDateTime timestamp;
} 