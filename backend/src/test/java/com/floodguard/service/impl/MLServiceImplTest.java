package com.floodguard.service.impl;

import com.floodguard.model.FloodPrediction;
import com.floodguard.model.Location;
import com.floodguard.model.RiskCategory;
import com.floodguard.repository.FloodPredictionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MLServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FloodPredictionRepository predictionRepository;

    @InjectMocks
    private MLServiceImpl mlService;

    private Location testLocation;
    private Map<String, Object> testPredictionResponse;
    private FloodPrediction testPrediction;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mlService, "mlServiceUrl", "http://localhost:5000");

        // Setup test location
        testLocation = new Location();
        testLocation.setId(1L);
        testLocation.setLatitude(12.34);
        testLocation.setLongitude(56.78);
        testLocation.setElevation(100.0);
        testLocation.setRainfall(50.0);
        testLocation.setSoilType("Clay");
        testLocation.setDrainageSystem("Good");

        // Setup test prediction response
        testPredictionResponse = new HashMap<>();
        testPredictionResponse.put("risk_score", 0.75);
        testPredictionResponse.put("risk_category", "HIGH");
        testPredictionResponse.put("confidence", 0.85);
        Map<String, Double> factors = new HashMap<>();
        factors.put("rainfall", 0.6);
        factors.put("elevation", 0.4);
        testPredictionResponse.put("factors", factors);

        // Setup test prediction
        testPrediction = new FloodPrediction();
        testPrediction.setLocation(testLocation);
        testPrediction.setRiskScore(0.75);
        testPrediction.setRiskCategory(RiskCategory.HIGH);
        testPrediction.setConfidence(0.85);
        testPrediction.setFactors(factors);
    }

    @Test
    void getPrediction_Success() {
        // Arrange
        when(restTemplate.postForEntity(
            eq("http://localhost:5000/predict"),
            eq(testLocation),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(testPredictionResponse, HttpStatus.OK));
        when(predictionRepository.save(any(FloodPrediction.class))).thenReturn(testPrediction);

        // Act
        FloodPrediction result = mlService.getPrediction(testLocation);

        // Assert
        assertNotNull(result);
        assertEquals(testLocation, result.getLocation());
        assertEquals(0.75, result.getRiskScore());
        assertEquals(RiskCategory.HIGH, result.getRiskCategory());
        assertEquals(0.85, result.getConfidence());
        assertEquals(2, result.getFactors().size());
        verify(predictionRepository).save(any(FloodPrediction.class));
    }

    @Test
    void getPrediction_ServiceError() {
        // Arrange
        when(restTemplate.postForEntity(
            eq("http://localhost:5000/predict"),
            eq(testLocation),
            eq(Map.class)
        )).thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> mlService.getPrediction(testLocation));
    }

    @Test
    void getLocationPredictions_Success() {
        // Arrange
        List<FloodPrediction> expectedPredictions = Arrays.asList(testPrediction);
        when(predictionRepository.findByLocationIdOrderByCreatedAtDesc(1L))
            .thenReturn(expectedPredictions);

        // Act
        List<FloodPrediction> result = mlService.getLocationPredictions(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPrediction, result.get(0));
    }

    @Test
    void getPredictionsByRisk_Success() {
        // Arrange
        List<FloodPrediction> expectedPredictions = Arrays.asList(testPrediction);
        when(predictionRepository.findByRiskCategory(RiskCategory.HIGH))
            .thenReturn(expectedPredictions);

        // Act
        List<FloodPrediction> result = mlService.getPredictionsByRisk(RiskCategory.HIGH);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPrediction, result.get(0));
    }

    @Test
    void getModelStatus_Success() {
        // Arrange
        Map<String, Object> statusResponse = new HashMap<>();
        statusResponse.put("status", "active");
        statusResponse.put("accuracy", 0.92);
        when(restTemplate.getForEntity(
            eq("http://localhost:5000/status"),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(statusResponse, HttpStatus.OK));

        // Act
        Map<String, Object> result = mlService.getModelStatus();

        // Assert
        assertNotNull(result);
        assertEquals("active", result.get("status"));
        assertEquals(0.92, result.get("accuracy"));
    }

    @Test
    void retrainModel_Success() {
        // Arrange
        Map<String, String> retrainResponse = new HashMap<>();
        retrainResponse.put("status", "training_started");
        retrainResponse.put("message", "Model retraining initiated");
        when(restTemplate.postForEntity(
            eq("http://localhost:5000/retrain"),
            eq(null),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(retrainResponse, HttpStatus.OK));

        // Act
        Map<String, String> result = mlService.retrainModel();

        // Assert
        assertNotNull(result);
        assertEquals("training_started", result.get("status"));
        assertEquals("Model retraining initiated", result.get("message"));
    }

    @Test
    void getFeatureImportance_Success() {
        // Arrange
        Map<String, Double> featureResponse = new HashMap<>();
        featureResponse.put("rainfall", 0.6);
        featureResponse.put("elevation", 0.4);
        when(restTemplate.getForEntity(
            eq("http://localhost:5000/features"),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(featureResponse, HttpStatus.OK));

        // Act
        Map<String, Double> result = mlService.getFeatureImportance();

        // Assert
        assertNotNull(result);
        assertEquals(0.6, result.get("rainfall"));
        assertEquals(0.4, result.get("elevation"));
    }
} 