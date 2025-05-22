package com.floodguard.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
} 