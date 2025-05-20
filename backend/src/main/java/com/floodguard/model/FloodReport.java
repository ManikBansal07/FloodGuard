package com.floodguard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "flood_reports")
public class FloodReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reporter;

    @Embedded
    private Location location;

    @NotBlank
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    private LocalDateTime reportedAt = LocalDateTime.now();

    private LocalDateTime verifiedAt;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    private String verificationNotes;
} 