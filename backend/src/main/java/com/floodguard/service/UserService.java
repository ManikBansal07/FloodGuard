package com.floodguard.service;

import com.floodguard.model.User;
import com.floodguard.dto.LoginRequest;
import com.floodguard.dto.LoginResponse;
import com.floodguard.dto.RegisterRequest;

public interface UserService {
    User registerUser(RegisterRequest request);
    LoginResponse loginUser(LoginRequest request);
    User getUserProfile(String token);
    User updateUserProfile(String token, User user);
} 