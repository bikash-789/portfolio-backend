package com.bikash.portfolio_backend.dto.auth;

import com.bikash.portfolio_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;
    private String email;
    private String name;
    private User.Role role;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String title;             
    private String description;       
    private String phone;             
    private String location;          
    private String profileImage;      
    private String heroImage;         
    private List<SocialLinkDto> socialLinks;  

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .title(user.getTitle())
                .description(user.getDescription())
                .phone(user.getPhone())
                .location(user.getLocation())
                .profileImage(user.getProfileImage())
                .heroImage(user.getHeroImage())
                .socialLinks(user.getSocialLinks() != null ? 
                    user.getSocialLinks().stream()
                        .map(SocialLinkDto::fromSocialLink)
                        .collect(Collectors.toList()) : null)
                .build();
    }
} 