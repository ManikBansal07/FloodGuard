package com.floodguard.controller;

import com.floodguard.model.FloodReport;
import com.floodguard.service.FloodReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class FloodReportController {

    @Autowired
    private FloodReportService floodReportService;

    @PostMapping
    public ResponseEntity<?> createReport(
            @RequestHeader("Authorization") String token,
            @RequestBody FloodReport report) {
        try {
            FloodReport createdReport = floodReportService.createReport(token, report);
            return ResponseEntity.ok(createdReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllReports(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String status) {
        try {
            List<FloodReport> reports = floodReportService.getAllReports(token, status);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            FloodReport report = floodReportService.getReportById(token, id);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody FloodReport report) {
        try {
            FloodReport updatedReport = floodReportService.updateReport(token, id, report);
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            floodReportService.deleteReport(token, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<?> getReportsByLocation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long locationId) {
        try {
            List<FloodReport> reports = floodReportService.getReportsByLocation(token, locationId);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 