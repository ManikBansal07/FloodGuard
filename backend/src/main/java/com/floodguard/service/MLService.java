package com.floodguard.service;

import com.floodguard.model.FloodPrediction;
import com.floodguard.model.Location;
import com.floodguard.model.RiskCategory;

import java.util.List;
import java.util.Map;

public interface MLService {
    FloodPrediction getPrediction(Location location);
    List<FloodPrediction> getLocationPredictions(Long locationId);
    List<FloodPrediction> getPredictionsByRisk(RiskCategory riskCategory);
    Map<String, Object> getModelStatus();
    Map<String, String> retrainModel();
    Map<String, Double> getFeatureImportance();
} 