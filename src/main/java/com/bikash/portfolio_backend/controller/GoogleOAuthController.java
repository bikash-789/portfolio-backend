package com.bikash.portfolio_backend.controller;

import com.bikash.portfolio_backend.dto.auth.AuthResponse;
import com.bikash.portfolio_backend.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthController {

    private final GoogleOAuthService googleOAuthService;

    /**
     * Initiate Google OAuth login
     * This endpoint redirects users to Google's OAuth consent screen
     */
    @GetMapping("/google")
    public void initiateGoogleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Initiating Google OAuth login");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        String redirectUrl = baseUrl + contextPath + "/oauth2/authorization/google";
        log.info("Redirecting to: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }



    /**
     * Get current OAuth user info (for testing/debugging)
     */
    @GetMapping("/google/user")
    public ResponseEntity<Map<String, Object>> getCurrentOAuthUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.ok(Map.of("message", "No OAuth user authenticated"));
        }

        Map<String, Object> userInfo = Map.of(
            "name", oauth2User.getAttribute("name"),
            "email", oauth2User.getAttribute("email"),
            "picture", oauth2User.getAttribute("picture"),
            "sub", oauth2User.getName()
        );

        return ResponseEntity.ok(userInfo);
    }
} 