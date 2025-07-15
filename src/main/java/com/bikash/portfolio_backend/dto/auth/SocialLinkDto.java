package com.bikash.portfolio_backend.dto.auth;

import com.bikash.portfolio_backend.entity.SocialLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinkDto {
    
    private String name;  // Social platform name (e.g., "GitHub", "LinkedIn")
    private String url;   // Social profile URL
    private String icon;  // Optional - Icon URL/path
    
    public static SocialLinkDto fromSocialLink(SocialLink socialLink) {
        if (socialLink == null) {
            return null;
        }
        return SocialLinkDto.builder()
                .name(socialLink.getName())
                .url(socialLink.getUrl())
                .icon(socialLink.getIcon())
                .build();
    }
    
    public SocialLink toSocialLink() {
        return SocialLink.builder()
                .name(this.name)
                .url(this.url)
                .icon(this.icon)
                .build();
    }
} 