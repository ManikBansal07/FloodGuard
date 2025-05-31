package com.floodguard.controller;

import com.floodguard.model.FloodReport;
import com.floodguard.model.User;
import com.floodguard.service.FloodReportService;
import com.floodguard.service.FileStorageService;
import com.floodguard.dto.ReportRequest;
import com.floodguard.dto.ReportResponse;
import com.floodguard.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class FloodReportController {

    @Autowired
    private FloodReportService reportService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReportResponse> submitReport(
            @CurrentUser User currentUser,
            @Valid @ModelAttribute ReportRequest reportRequest,
            @RequestParam(required = false) MultipartFile image) {
        
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image);
        }

        FloodReport report = reportService.createReport(currentUser, reportRequest, imageUrl);
        return ResponseEntity.ok(new ReportResponse(report));
    }

    @GetMapping
    public ResponseEntity<Page<ReportResponse>> getAllReports(Pageable pageable) {
        Page<ReportResponse> reports = reportService.getAllReports(pageable)
            .map(ReportResponse::new);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ReportResponse>> getMyReports(
            @CurrentUser User currentUser,
            Pageable pageable) {
        Page<ReportResponse> reports = reportService.getUserReports(currentUser, pageable)
            .map(ReportResponse::new);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<ReportResponse>> getNearbyReports(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5000") Double radius) {
        List<ReportResponse> reports = reportService.getNearbyReports(latitude, longitude, radius)
            .stream()
            .map(ReportResponse::new)
            .toList();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) {
        FloodReport report = reportService.getReport(id);
        return ResponseEntity.ok(new ReportResponse(report));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReportResponse> updateReport(
            @CurrentUser User currentUser,
            @PathVariable Long id,
            @Valid @ModelAttribute ReportRequest reportRequest,
            @RequestParam(required = false) MultipartFile image) {
        
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileStorageService.storeFile(image);
        }

        FloodReport report = reportService.updateReport(currentUser, id, reportRequest, imageUrl);
        return ResponseEntity.ok(new ReportResponse(report));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteReport(
            @CurrentUser User currentUser,
            @PathVariable Long id) {
        reportService.deleteReport(currentUser, id);
        return ResponseEntity.ok().build();
    }
} 