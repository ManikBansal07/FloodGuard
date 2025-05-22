package com.floodguard.repository;

import com.floodguard.model.FloodReport;
import com.floodguard.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloodReportRepository extends JpaRepository<FloodReport, Long> {
    List<FloodReport> findByStatus(ReportStatus status);
    List<FloodReport> findByLocationId(Long locationId);
    List<FloodReport> findByReporterId(Long reporterId);
} 