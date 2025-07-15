package com.bikash.portfolio_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonalInfoRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;

    @Size(max = 200, message = "Location must be less than 200 characters")
    private String location;

    @Size(max = 500, message = "Profile image URL must be less than 500 characters")
    private String profileImage;

    @Size(max = 500, message = "Hero image URL must be less than 500 characters")
    private String heroImage;

    private List<SocialLinkDto> socialLinks;
} 