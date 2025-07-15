package com.bikash.portfolio_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String name;

    private String password;

    @Builder.Default
    private Role role = Role.USER;

    private String refreshToken;

    @Builder.Default
    private boolean emailVerified = false;

    private String emailVerificationToken;

    private LocalDateTime emailVerificationExpiry;

    private String passwordResetToken;

    private LocalDateTime passwordResetExpiry;
    private String googleId;
    private String provider;

    // PersonalInfo fields
    private String title;             // Job title (e.g., "Full Stack Developer")
    private String description;       // Brief description/bio
    private String phone;             // Phone number
    private String location;          // Location (e.g., "San Francisco, CA")
    private String profileImage;      // Profile image URL/path
    private String heroImage;         // Hero section background image URL/path
    private List<SocialLink> socialLinks;  // Array of social media links

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role {
        USER, ADMIN
    }
} 