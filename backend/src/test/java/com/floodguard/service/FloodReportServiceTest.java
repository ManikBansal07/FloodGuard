package com.floodguard.service;

import com.floodguard.dto.ReportRequest;
import com.floodguard.model.FloodReport;
import com.floodguard.model.User;
import com.floodguard.repository.FloodReportRepository;
import com.floodguard.exception.ResourceNotFoundException;
import com.floodguard.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FloodReportServiceTest {

    @Mock
    private FloodReportRepository reportRepository;

    @InjectMocks
    private FloodReportService reportService;

    private User testUser;
    private User adminUser;
    private FloodReport testReport;
    private ReportRequest reportRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole(User.Role.USER);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");
        adminUser.setRole(User.Role.ADMIN);

        testReport = new FloodReport();
        testReport.setId(1L);
        testReport.setTitle("Test Report");
        testReport.setDescription("Test Description");
        testReport.setLatitude(37.7749);
        testReport.setLongitude(-122.4194);
        testReport.setSeverity(FloodReport.Severity.MEDIUM);
        testReport.setStatus(FloodReport.Status.PENDING);
        testReport.setReporter(testUser);

        reportRequest = new ReportRequest();
        reportRequest.setTitle("New Report");
        reportRequest.setDescription("New Description");
        reportRequest.setLatitude(37.7749);
        reportRequest.setLongitude(-122.4194);
        reportRequest.setSeverity(FloodReport.Severity.HIGH);
    }

    @Test
    void createReport_Success() {
        when(reportRepository.save(any(FloodReport.class))).thenReturn(testReport);

        FloodReport result = reportService.createReport(testUser, reportRequest, null);

        assertNotNull(result);
        assertEquals(testUser, result.getReporter());
        assertEquals(FloodReport.Status.PENDING, result.getStatus());
        verify(reportRepository).save(any(FloodReport.class));
    }

    @Test
    void getReport_Success() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));

        FloodReport result = reportService.getReport(1L);

        assertNotNull(result);
        assertEquals(testReport.getId(), result.getId());
    }

    @Test
    void getReport_NotFound() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.getReport(1L));
    }

    @Test
    void updateReport_Success() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));
        when(reportRepository.save(any(FloodReport.class))).thenReturn(testReport);

        FloodReport result = reportService.updateReport(testUser, 1L, reportRequest, null);

        assertNotNull(result);
        verify(reportRepository).save(any(FloodReport.class));
    }

    @Test
    void updateReport_Unauthorized() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));

        assertThrows(UnauthorizedException.class, 
            () -> reportService.updateReport(adminUser, 1L, reportRequest, null));
    }

    @Test
    void deleteReport_Success() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));
        doNothing().when(reportRepository).delete(testReport);

        assertDoesNotThrow(() -> reportService.deleteReport(testUser, 1L));
        verify(reportRepository).delete(testReport);
    }

    @Test
    void deleteReport_Unauthorized() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));

        assertThrows(UnauthorizedException.class, 
            () -> reportService.deleteReport(adminUser, 1L));
    }

    @Test
    void approveReport_Success() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(testReport));
        when(reportRepository.save(any(FloodReport.class))).thenReturn(testReport);

        FloodReport result = reportService.approveReport(adminUser, 1L);

        assertNotNull(result);
        assertEquals(FloodReport.Status.VERIFIED, result.getStatus());
        assertEquals(adminUser, result.getVerifiedBy());
        verify(reportRepository).save(any(FloodReport.class));
    }

    @Test
    void getAllReports_Success() {
        Page<FloodReport> page = new PageImpl<>(Arrays.asList(testReport));
        when(reportRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<FloodReport> result = reportService.getAllReports(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
} 