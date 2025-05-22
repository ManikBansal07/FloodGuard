package com.floodguard.service.impl;

import com.floodguard.model.User;
import com.floodguard.model.UserRole;
import com.floodguard.repository.UserRepository;
import com.floodguard.service.UserService;
import com.floodguard.dto.LoginRequest;
import com.floodguard.dto.LoginResponse;
import com.floodguard.dto.RegisterRequest;
import com.floodguard.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(authentication);

        return LoginResponse.builder()
            .token(token)
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole().toString())
            .build();
    }

    @Override
    public User getUserProfile(String token) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User updateUserProfile(String token, User updatedUser) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedUser.getName());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setAddress(updatedUser.getAddress());

        return userRepository.save(user);
    }
} 