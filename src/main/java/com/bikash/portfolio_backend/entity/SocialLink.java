package com.bikash.portfolio_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLink {
    
    private String name;  // Social platform name (e.g., "GitHub", "LinkedIn")
    private String url;   // Social profile URL
    private String icon;  // Optional - Icon URL/path
} 