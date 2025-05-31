package com.floodguard.dto;

import com.floodguard.model.FloodReport;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportResponse {
    private Long id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private FloodReport.Severity severity;
    private FloodReport.Status status;
    private String reporterUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime verifiedAt;
    private String verifiedByUsername;

    public ReportResponse(FloodReport report) {
        this.id = report.getId();
        this.title = report.getTitle();
        this.description = report.getDescription();
        this.latitude = report.getLatitude();
        this.longitude = report.getLongitude();
        this.imageUrl = report.getImageUrl();
        this.severity = report.getSeverity();
        this.status = report.getStatus();
        this.reporterUsername = report.getReporter().getUsername();
        this.createdAt = report.getCreatedAt();
        this.updatedAt = report.getUpdatedAt();
        this.verifiedAt = report.getVerifiedAt();
        if (report.getVerifiedBy() != null) {
            this.verifiedByUsername = report.getVerifiedBy().getUsername();
        }
    }
} 