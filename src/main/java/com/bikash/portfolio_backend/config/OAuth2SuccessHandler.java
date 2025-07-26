package com.bikash.portfolio_backend.config;

import com.bikash.portfolio_backend.service.GoogleOAuthService;
import com.bikash.portfolio_backend.dto.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final GoogleOAuthService googleOAuthService;
    private final AdminEmailConfig adminEmailConfig;
    
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = oauth2User.getAttribute("email");
        log.info("OAuth2 authentication successful for user: {}", userEmail);
        
        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String googleId = oauth2User.getName();
            
            log.info("Processing OAuth2 user - Email: {}, Name: {}, GoogleId: {}", email, name, googleId);
            
            if (email == null || name == null) {
                log.error("Missing required attributes from Google OAuth");
                redirectToErrorPage(response, "Missing required user information");
                return;
            }
            
            if (!adminEmailConfig.isEmailAllowed(email)) {
                log.warn("OAuth login attempt denied for email: {} - not in allowed admin emails list", email);
                redirectToErrorPage(response, "Access denied. Your email is not authorized for admin access.");
                return;
            }
            
            AuthResponse authResponse = googleOAuthService.processGoogleAuth(email, name, googleId);
            
            String frontendUrl = allowedOrigins.split(",")[0].trim();
            
            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/callback")
                    .queryParam("token", authResponse.getToken())
                    .queryParam("refreshToken", authResponse.getRefreshToken())
                    .queryParam("user", URLEncoder.encode(authResponse.getUser().getName(), StandardCharsets.UTF_8))
                    .build().toUriString();
            
            log.info("Redirecting to frontend: {}", redirectUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            
        } catch (Exception e) {
            log.error("Error processing OAuth2 authentication", e);
            redirectToErrorPage(response, "Authentication processing failed");
        }
    }
    
    private void redirectToErrorPage(HttpServletResponse response, String error) throws IOException {
        String frontendUrl = allowedOrigins.split(",")[0].trim();
        
        String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/error")
                .queryParam("error", URLEncoder.encode(error, StandardCharsets.UTF_8))
                .build().toUriString();
        
        response.sendRedirect(errorUrl);
    }
} 