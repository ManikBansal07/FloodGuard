package com.floodguard.controller;

import com.floodguard.model.FloodPrediction;
import com.floodguard.model.Location;
import com.floodguard.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "*")
public class PredictionController {

    @Autowired
    private MLService mlService;

    @PostMapping("/predict")
    public ResponseEntity<?> predictFloodRisk(
            @RequestHeader("Authorization") String token,
            @RequestBody Location location) {
        try {
            FloodPrediction prediction = mlService.predictFloodRisk(location);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/historical/{locationId}")
    public ResponseEntity<?> getHistoricalPrediction(
            @RequestHeader("Authorization") String token,
            @PathVariable Long locationId) {
        try {
            FloodPrediction prediction = mlService.getHistoricalPrediction(locationId);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-model")
    public ResponseEntity<?> updateModel(
            @RequestHeader("Authorization") String token) {
        try {
            mlService.updateModel();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 