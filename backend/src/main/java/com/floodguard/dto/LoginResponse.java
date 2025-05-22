package com.floodguard.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String email;
    private String name;
    private String role;
} 