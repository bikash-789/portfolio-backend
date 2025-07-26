package com.bikash.portfolio_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.admin")
@Data
public class AdminEmailConfig {
    private List<String> allowedEmails;
    public boolean isEmailAllowed(String email) {
        if (allowedEmails == null || allowedEmails.isEmpty()) {
            return false;
        }
        return allowedEmails.contains(email.toLowerCase().trim());
    }
} 