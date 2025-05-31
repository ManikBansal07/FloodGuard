package com.floodguard.controller;

import com.floodguard.model.User;
import com.floodguard.service.FloodReportService;
import com.floodguard.dto.ReportResponse;
import com.floodguard.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private FloodReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<Page<ReportResponse>> getAllReports(Pageable pageable) {
        Page<ReportResponse> reports = reportService.getAllReports(pageable)
            .map(ReportResponse::new);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/reports/{id}/approve")
    public ResponseEntity<ReportResponse> approveReport(
            @CurrentUser User currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(new ReportResponse(reportService.approveReport(currentUser, id)));
    }

    @PostMapping("/reports/{id}/reject")
    public ResponseEntity<ReportResponse> rejectReport(
            @CurrentUser User currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(new ReportResponse(reportService.rejectReport(currentUser, id)));
    }
} 