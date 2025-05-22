package com.floodguard.service.impl;

import com.floodguard.exception.ResourceNotFoundException;
import com.floodguard.model.FloodPrediction;
import com.floodguard.model.Location;
import com.floodguard.model.RiskCategory;
import com.floodguard.repository.FloodPredictionRepository;
import com.floodguard.service.MLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MLServiceImpl implements MLService {

    private final FloodPredictionRepository predictionRepository;
    private final RestTemplate restTemplate;

    @Value("${app.mlServiceUrl}")
    private String mlServiceUrl;

    @Override
    @Cacheable(value = "predictions", key = "#location.id", unless = "#result == null")
    public FloodPrediction predictFlood(Location location) {
        log.debug("Predicting flood for location: {}", location.getId());
        try {
            ResponseEntity<FloodPrediction> response = restTemplate.postForEntity(
                mlServiceUrl + "/predict",
                location,
                FloodPrediction.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error predicting flood for location {}: {}", location.getId(), e.getMessage());
            throw new RuntimeException("Failed to get prediction from ML service", e);
        }
    }

    @Override
    @Cacheable(value = "predictions", key = "'batch_' + #locations.hashCode()", unless = "#result == null")
    public List<FloodPrediction> predictFloodsBatch(List<Location> locations) {
        log.debug("Predicting floods for {} locations", locations.size());
        try {
            ResponseEntity<List<FloodPrediction>> response = restTemplate.postForEntity(
                mlServiceUrl + "/predict/batch",
                locations,
                List.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error predicting floods for batch: {}", e.getMessage());
            throw new RuntimeException("Failed to get batch predictions from ML service", e);
        }
    }

    @Override
    @Cacheable(value = "predictions", key = "'model_info'", unless = "#result == null")
    public Map<String, Object> getModelInfo() {
        log.debug("Getting model information");
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                mlServiceUrl + "/model/info",
                Map.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error getting model info: {}", e.getMessage());
            throw new RuntimeException("Failed to get model information", e);
        }
    }

    @Override
    @Cacheable(value = "predictions", key = "'feature_importance'", unless = "#result == null")
    public Map<String, Double> getFeatureImportance() {
        log.debug("Getting feature importance");
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                mlServiceUrl + "/model/features",
                Map.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error getting feature importance: {}", e.getMessage());
            throw new RuntimeException("Failed to get feature importance", e);
        }
    }

    @Override
    public List<FloodPrediction> getLocationPredictions(Long locationId) {
        return predictionRepository.findByLocationIdOrderByCreatedAtDesc(locationId);
    }

    @Override
    public List<FloodPrediction> getPredictionsByRisk(RiskCategory riskCategory) {
        return predictionRepository.findByRiskCategory(riskCategory);
    }

    @Override
    public Map<String, Object> getModelStatus() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                mlServiceUrl + "/status",
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("Failed to get model status");
        } catch (Exception e) {
            log.error("Error getting model status", e);
            throw new RuntimeException("Failed to get model status", e);
        }
    }

    @Override
    public Map<String, String> retrainModel() {
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                mlServiceUrl + "/retrain",
                null,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (Map<String, String>) response.getBody();
            }
            throw new RuntimeException("Failed to retrain model");
        } catch (Exception e) {
            log.error("Error retraining model", e);
            throw new RuntimeException("Failed to retrain model", e);
        }
    }
} 