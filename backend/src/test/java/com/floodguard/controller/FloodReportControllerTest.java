package com.floodguard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floodguard.model.FloodReport;
import com.floodguard.model.User;
import com.floodguard.service.FloodReportService;
import com.floodguard.service.FileStorageService;
import com.floodguard.dto.ReportRequest;
import com.floodguard.dto.ReportResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FloodReportController.class)
class FloodReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FloodReportService reportService;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    @WithMockUser(roles = "USER")
    void submitReport_Success() throws Exception {
        ReportRequest request = new ReportRequest();
        request.setTitle("Test Report");
        request.setDescription("Test Description");
        request.setLatitude(37.7749);
        request.setLongitude(-122.4194);
        request.setSeverity(FloodReport.Severity.MEDIUM);

        FloodReport report = new FloodReport();
        report.setId(1L);
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setSeverity(request.getSeverity());

        when(reportService.createReport(any(), any(), any())).thenReturn(report);

        MockMultipartFile file = new MockMultipartFile(
            "image",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/reports")
                .file(file)
                .param("title", request.getTitle())
                .param("description", request.getDescription())
                .param("latitude", request.getLatitude().toString())
                .param("longitude", request.getLongitude().toString())
                .param("severity", request.getSeverity().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(request.getTitle()));
    }

    @Test
    void getAllReports_Success() throws Exception {
        FloodReport report = new FloodReport();
        report.setId(1L);
        report.setTitle("Test Report");
        report.setDescription("Test Description");
        report.setLatitude(37.7749);
        report.setLongitude(-122.4194);
        report.setSeverity(FloodReport.Severity.MEDIUM);

        Page<FloodReport> page = new PageImpl<>(Arrays.asList(report));
        when(reportService.getAllReports(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/reports")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Report"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getMyReports_Success() throws Exception {
        FloodReport report = new FloodReport();
        report.setId(1L);
        report.setTitle("Test Report");
        report.setDescription("Test Description");

        Page<FloodReport> page = new PageImpl<>(Arrays.asList(report));
        when(reportService.getUserReports(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/reports/my")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Report"));
    }

    @Test
    void getNearbyReports_Success() throws Exception {
        FloodReport report = new FloodReport();
        report.setId(1L);
        report.setTitle("Test Report");
        report.setDescription("Test Description");
        report.setLatitude(37.7749);
        report.setLongitude(-122.4194);

        List<FloodReport> reports = Arrays.asList(report);
        when(reportService.getNearbyReports(any(), any(), any())).thenReturn(reports);

        mockMvc.perform(get("/api/reports/nearby")
                .param("latitude", "37.7749")
                .param("longitude", "-122.4194")
                .param("radius", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Report"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateReport_Success() throws Exception {
        ReportRequest request = new ReportRequest();
        request.setTitle("Updated Report");
        request.setDescription("Updated Description");
        request.setLatitude(37.7749);
        request.setLongitude(-122.4194);
        request.setSeverity(FloodReport.Severity.HIGH);

        FloodReport report = new FloodReport();
        report.setId(1L);
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setSeverity(request.getSeverity());

        when(reportService.updateReport(any(), any(), any(), any())).thenReturn(report);

        MockMultipartFile file = new MockMultipartFile(
            "image",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/reports/1")
                .file(file)
                .param("title", request.getTitle())
                .param("description", request.getDescription())
                .param("latitude", request.getLatitude().toString())
                .param("longitude", request.getLongitude().toString())
                .param("severity", request.getSeverity().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(request.getTitle()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteReport_Success() throws Exception {
        mockMvc.perform(delete("/api/reports/1"))
                .andExpect(status().isOk());
    }
} 