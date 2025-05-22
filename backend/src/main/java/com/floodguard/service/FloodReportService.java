package com.floodguard.service;

import com.floodguard.model.FloodReport;
import java.util.List;

public interface FloodReportService {
    FloodReport createReport(String token, FloodReport report);
    List<FloodReport> getAllReports(String token, String status);
    FloodReport getReportById(String token, Long id);
    FloodReport updateReport(String token, Long id, FloodReport report);
    void deleteReport(String token, Long id);
    List<FloodReport> getReportsByLocation(String token, Long locationId);
} 