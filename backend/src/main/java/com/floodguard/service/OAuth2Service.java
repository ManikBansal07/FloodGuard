package com.floodguard.service;

import com.floodguard.dto.AuthResponse;
import com.floodguard.model.User;
import com.floodguard.repository.UserRepository;
import com.floodguard.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oauth2User = super.loadUser(userRequest);
            log.info("OAuth2 user loaded successfully: {}", oauth2User.getAttributes());
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception ex) {
            log.error("Error loading OAuth2 user", ex);
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        try {
            Map<String, Object> attributes = oauth2User.getAttributes();
            User user = getOrCreateUser(attributes);
            
            return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attributes,
                "email"
            );
        } catch (Exception ex) {
            log.error("Error processing OAuth2 user", ex);
            throw new OAuth2AuthenticationException("Error processing OAuth2 user: " + ex.getMessage());
        }
    }

    public AuthResponse getAuthResponse(User user) {
        try {
            String token = jwtService.generateToken(user);
            return new AuthResponse(token);
        } catch (Exception ex) {
            log.error("Error generating auth response", ex);
            throw new OAuth2AuthenticationException("Error generating auth response: " + ex.getMessage());
        }
    }

    public User getOrCreateUser(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found in OAuth2 user attributes");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setFullName(name);
            user.setEmailVerified(true);
            log.info("Existing user found and updated: {}", email);
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(email.split("@")[0]);
            user.setFullName(name);
            user.setEmailVerified(true);
            user.setRole(User.Role.USER);
            user.setEnabled(true);
            user.setPassword(java.util.UUID.randomUUID().toString());
            log.info("New user created: {}", email);
        }

        return userRepository.save(user);
    }
} 