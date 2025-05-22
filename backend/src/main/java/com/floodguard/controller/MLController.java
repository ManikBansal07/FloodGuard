package com.floodguard.controller;

import com.floodguard.model.FloodPrediction;
import com.floodguard.model.Location;
import com.floodguard.model.RiskCategory;
import com.floodguard.service.MLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ml")
@RequiredArgsConstructor
@Tag(name = "ML Service", description = "Endpoints for flood prediction and model management")
public class MLController {

    private final MLService mlService;

    @PostMapping("/predict")
    @Operation(summary = "Get flood prediction for a location")
    public ResponseEntity<FloodPrediction> getPrediction(@RequestBody Location location) {
        return ResponseEntity.ok(mlService.getPrediction(location));
    }

    @GetMapping("/predictions/location/{locationId}")
    @Operation(summary = "Get all predictions for a location")
    public ResponseEntity<List<FloodPrediction>> getLocationPredictions(@PathVariable Long locationId) {
        return ResponseEntity.ok(mlService.getLocationPredictions(locationId));
    }

    @GetMapping("/predictions/risk/{riskCategory}")
    @Operation(summary = "Get predictions by risk category")
    public ResponseEntity<List<FloodPrediction>> getPredictionsByRisk(@PathVariable RiskCategory riskCategory) {
        return ResponseEntity.ok(mlService.getPredictionsByRisk(riskCategory));
    }

    @GetMapping("/model/status")
    @Operation(summary = "Get model status and metrics")
    public ResponseEntity<Map<String, Object>> getModelStatus() {
        return ResponseEntity.ok(mlService.getModelStatus());
    }

    @PostMapping("/model/retrain")
    @Operation(summary = "Trigger model retraining")
    public ResponseEntity<Map<String, String>> retrainModel() {
        return ResponseEntity.ok(mlService.retrainModel());
    }

    @GetMapping("/model/features")
    @Operation(summary = "Get model feature importance")
    public ResponseEntity<Map<String, Double>> getFeatureImportance() {
        return ResponseEntity.ok(mlService.getFeatureImportance());
    }
} 