package com.floodguard.repository;

import com.floodguard.model.FloodReport;
import com.floodguard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloodReportRepository extends JpaRepository<FloodReport, Long> {
    Page<FloodReport> findByReporter(User reporter, Pageable pageable);
    Page<FloodReport> findByStatus(FloodReport.Status status, Pageable pageable);
    
    @Query("SELECT f FROM FloodReport f WHERE f.status = 'VERIFIED' AND " +
           "ST_Distance_Sphere(point(f.longitude, f.latitude), point(:longitude, :latitude)) <= :radius")
    List<FloodReport> findNearbyVerifiedReports(Double latitude, Double longitude, Double radius);
    
    @Query("SELECT COUNT(f) FROM FloodReport f WHERE f.status = 'VERIFIED' AND " +
           "ST_Distance_Sphere(point(f.longitude, f.latitude), point(:longitude, :latitude)) <= :radius")
    long countNearbyVerifiedReports(Double latitude, Double longitude, Double radius);
} 