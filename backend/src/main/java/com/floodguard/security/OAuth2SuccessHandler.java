package com.floodguard.security;

import com.floodguard.dto.AuthResponse;
import com.floodguard.model.User;
import com.floodguard.service.OAuth2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2Service oauth2Service;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            log.debug("OAuth2 user attributes: {}", oauth2User.getAttributes());
            
            Map<String, Object> attributes = oauth2User.getAttributes();
            String email = (String) attributes.get("email");
            
            if (email == null) {
                throw new RuntimeException("Email not found in OAuth2 attributes");
            }

            User user = oauth2Service.getOrCreateUser(attributes);
            AuthResponse authResponse = oauth2Service.getAuthResponse(user);
            
            // Redirect to frontend callback URL with token
            String frontendUrl = "http://localhost:3000/oauth2/callback";
            String redirectUrl = frontendUrl + "?token=" + authResponse.getToken();
            response.sendRedirect(redirectUrl);
            
            log.info("OAuth2 authentication successful for user: {}", user.getEmail());
        } catch (Exception ex) {
            log.error("Error in OAuth2 success handler: {}", ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\",\"details\":\"" + ex.getClass().getName() + "\"}");
        }
    }
} 