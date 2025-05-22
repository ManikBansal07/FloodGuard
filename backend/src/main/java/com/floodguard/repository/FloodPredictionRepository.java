package com.floodguard.repository;

import com.floodguard.model.FloodPrediction;
import com.floodguard.model.RiskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloodPredictionRepository extends JpaRepository<FloodPrediction, Long> {
    List<FloodPrediction> findByLocationId(Long locationId);
    List<FloodPrediction> findByRiskCategory(RiskCategory riskCategory);
    List<FloodPrediction> findByLocationIdOrderByCreatedAtDesc(Long locationId);
} 