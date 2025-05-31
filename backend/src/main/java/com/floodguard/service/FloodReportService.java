package com.floodguard.service;

import com.floodguard.dto.ReportRequest;
import com.floodguard.model.FloodReport;
import com.floodguard.model.User;
import com.floodguard.repository.FloodReportRepository;
import com.floodguard.exception.ResourceNotFoundException;
import com.floodguard.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FloodReportService {

    @Autowired
    private FloodReportRepository reportRepository;

    @Transactional
    public FloodReport createReport(User currentUser, ReportRequest request, String imageUrl) {
        FloodReport report = new FloodReport();
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setSeverity(request.getSeverity());
        report.setStatus(FloodReport.Status.PENDING);
        report.setReporter(currentUser);
        report.setImageUrl(imageUrl);
        return reportRepository.save(report);
    }

    public Page<FloodReport> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    public Page<FloodReport> getUserReports(User user, Pageable pageable) {
        return reportRepository.findByReporter(user, pageable);
    }

    public List<FloodReport> getNearbyReports(Double latitude, Double longitude, Double radius) {
        return reportRepository.findNearbyVerifiedReports(latitude, longitude, radius);
    }

    public FloodReport getReport(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    @Transactional
    public FloodReport updateReport(User currentUser, Long id, ReportRequest request, String imageUrl) {
        FloodReport report = getReport(id);
        
        if (!report.getReporter().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to update this report");
        }

        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setSeverity(request.getSeverity());
        if (imageUrl != null) {
            report.setImageUrl(imageUrl);
        }
        
        return reportRepository.save(report);
    }

    @Transactional
    public void deleteReport(User currentUser, Long id) {
        FloodReport report = getReport(id);
        
        if (!report.getReporter().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this report");
        }

        reportRepository.delete(report);
    }

    @Transactional
    public FloodReport approveReport(User admin, Long id) {
        FloodReport report = getReport(id);
        report.setStatus(FloodReport.Status.VERIFIED);
        report.setVerifiedAt(LocalDateTime.now());
        report.setVerifiedBy(admin);
        return reportRepository.save(report);
    }

    @Transactional
    public FloodReport rejectReport(User admin, Long id) {
        FloodReport report = getReport(id);
        report.setStatus(FloodReport.Status.REJECTED);
        report.setVerifiedAt(LocalDateTime.now());
        report.setVerifiedBy(admin);
        return reportRepository.save(report);
    }
} 