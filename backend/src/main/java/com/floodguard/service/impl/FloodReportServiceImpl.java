package com.floodguard.service.impl;

import com.floodguard.model.FloodReport;
import com.floodguard.model.User;
import com.floodguard.model.ReportStatus;
import com.floodguard.repository.FloodReportRepository;
import com.floodguard.repository.UserRepository;
import com.floodguard.security.JwtTokenProvider;
import com.floodguard.service.FloodReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FloodReportServiceImpl implements FloodReportService {

    @Autowired
    private FloodReportRepository floodReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public FloodReport createReport(String token, FloodReport report) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        report.setReporter(user);
        report.setStatus(ReportStatus.PENDING);
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        return floodReportRepository.save(report);
    }

    @Override
    public List<FloodReport> getAllReports(String token, String status) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (status != null && !status.isEmpty()) {
            return floodReportRepository.findByStatus(ReportStatus.valueOf(status.toUpperCase()));
        }
        return floodReportRepository.findAll();
    }

    @Override
    public FloodReport getReportById(String token, Long id) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return floodReportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public FloodReport updateReport(String token, Long id, FloodReport updatedReport) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        FloodReport existingReport = floodReportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        // Only allow updates if user is the reporter or an admin
        if (!existingReport.getReporter().equals(user) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to update this report");
        }

        existingReport.setDescription(updatedReport.getDescription());
        existingReport.setSeverity(updatedReport.getSeverity());
        existingReport.setStatus(updatedReport.getStatus());
        existingReport.setUpdatedAt(LocalDateTime.now());

        return floodReportRepository.save(existingReport);
    }

    @Override
    public void deleteReport(String token, Long id) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        FloodReport report = floodReportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        // Only allow deletion if user is the reporter or an admin
        if (!report.getReporter().equals(user) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to delete this report");
        }

        floodReportRepository.delete(report);
    }

    @Override
    public List<FloodReport> getReportsByLocation(String token, Long locationId) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return floodReportRepository.findByLocationId(locationId);
    }
} 