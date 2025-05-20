package com.floodguard.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Location {
    private Double latitude;
    private Double longitude;
    private String address;
    private String locationDescription;
} 