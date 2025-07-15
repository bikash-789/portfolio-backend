package com.bikash.portfolio_backend.controller;

import com.bikash.portfolio_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> testEmail(@RequestBody Map<String, String> request) {
        try {
            String toEmail = request.get("email");
            String testType = request.getOrDefault("type", "verification");
            
            if (toEmail == null || toEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email address is required"));
            }
            
            switch (testType) {
                case "verification":
                    emailService.sendEmailVerification(toEmail, "test-verification-token");
                    break;
                case "reset":
                    emailService.sendPasswordResetEmail(toEmail, "test-reset-token");
                    break;
                case "contact":
                    emailService.sendContactNotification(toEmail, "Test User", "test@example.com", "Test Contact");
                    break;
                default:
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid test type. Use 'verification', 'reset', or 'contact'"));
            }
            
            log.info("Test email sent successfully to: {}", toEmail);
            return ResponseEntity.ok(Map.of(
                "message", "Test email sent successfully",
                "email", toEmail,
                "type", testType
            ));
            
        } catch (Exception e) {
            log.error("Failed to send test email", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to send test email: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "healthy",
            "service", "Test Controller",
            "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }
} 