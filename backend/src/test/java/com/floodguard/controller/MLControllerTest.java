package com.floodguard.controller;

import com.floodguard.model.FloodPrediction;
import com.floodguard.model.Location;
import com.floodguard.model.RiskCategory;
import com.floodguard.service.MLService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MLControllerTest {

    @Mock
    private MLService mlService;

    @InjectMocks
    private MLController mlController;

    private MockMvc mockMvc;
    private Location testLocation;
    private FloodPrediction testPrediction;
    private Map<String, Object> testModelStatus;
    private Map<String, String> testRetrainResponse;
    private Map<String, Double> testFeatureImportance;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mlController).build();

        // Setup test location
        testLocation = new Location();
        testLocation.setId(1L);
        testLocation.setLatitude(12.34);
        testLocation.setLongitude(56.78);
        testLocation.setElevation(100.0);
        testLocation.setRainfall(50.0);
        testLocation.setSoilType("Clay");
        testLocation.setDrainageSystem("Good");

        // Setup test prediction
        testPrediction = new FloodPrediction();
        testPrediction.setLocation(testLocation);
        testPrediction.setRiskScore(0.75);
        testPrediction.setRiskCategory(RiskCategory.HIGH);
        testPrediction.setConfidence(0.85);
        Map<String, Double> factors = new HashMap<>();
        factors.put("rainfall", 0.6);
        factors.put("elevation", 0.4);
        testPrediction.setFactors(factors);

        // Setup test model status
        testModelStatus = new HashMap<>();
        testModelStatus.put("status", "active");
        testModelStatus.put("accuracy", 0.92);

        // Setup test retrain response
        testRetrainResponse = new HashMap<>();
        testRetrainResponse.put("status", "training_started");
        testRetrainResponse.put("message", "Model retraining initiated");

        // Setup test feature importance
        testFeatureImportance = new HashMap<>();
        testFeatureImportance.put("rainfall", 0.6);
        testFeatureImportance.put("elevation", 0.4);
    }

    @Test
    void getPrediction_Success() throws Exception {
        when(mlService.getPrediction(any(Location.class))).thenReturn(testPrediction);

        mockMvc.perform(post("/api/ml/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"latitude\":12.34,\"longitude\":56.78,\"elevation\":100.0,\"rainfall\":50.0,\"soilType\":\"Clay\",\"drainageSystem\":\"Good\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskScore").value(0.75))
                .andExpect(jsonPath("$.riskCategory").value("HIGH"))
                .andExpect(jsonPath("$.confidence").value(0.85));
    }

    @Test
    void getLocationPredictions_Success() throws Exception {
        when(mlService.getLocationPredictions(1L)).thenReturn(Collections.singletonList(testPrediction));

        mockMvc.perform(get("/api/ml/predictions/location/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].riskScore").value(0.75))
                .andExpect(jsonPath("$[0].riskCategory").value("HIGH"));
    }

    @Test
    void getPredictionsByRisk_Success() throws Exception {
        when(mlService.getPredictionsByRisk(RiskCategory.HIGH)).thenReturn(Collections.singletonList(testPrediction));

        mockMvc.perform(get("/api/ml/predictions/risk/HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].riskScore").value(0.75))
                .andExpect(jsonPath("$[0].riskCategory").value("HIGH"));
    }

    @Test
    void getModelStatus_Success() throws Exception {
        when(mlService.getModelStatus()).thenReturn(testModelStatus);

        mockMvc.perform(get("/api/ml/model/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.accuracy").value(0.92));
    }

    @Test
    void retrainModel_Success() throws Exception {
        when(mlService.retrainModel()).thenReturn(testRetrainResponse);

        mockMvc.perform(post("/api/ml/model/retrain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("training_started"))
                .andExpect(jsonPath("$.message").value("Model retraining initiated"));
    }

    @Test
    void getFeatureImportance_Success() throws Exception {
        when(mlService.getFeatureImportance()).thenReturn(testFeatureImportance);

        mockMvc.perform(get("/api/ml/model/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rainfall").value(0.6))
                .andExpect(jsonPath("$.elevation").value(0.4));
    }
} 