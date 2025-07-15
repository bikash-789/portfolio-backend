package com.bikash.portfolio_backend.dto.status;

import com.bikash.portfolio_backend.entity.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {

    private String id;

    private String emoji;

    private String message;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("predefinedStatusId")
    private String predefinedStatusId;

    @JsonProperty("expiresAt")
    private LocalDateTime expiresAt;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("lastUpdated")
    private LocalDateTime lastUpdated;

    public static StatusDto fromStatus(Status status) {
        return StatusDto.builder()
                .id(status.getId())
                .emoji(status.getEmoji())
                .message(status.getMessage())
                .isActive(status.getIsActive())
                .predefinedStatusId(status.getPredefinedStatusId())
                .expiresAt(status.getExpiresAt())
                .createdAt(status.getCreatedAt())
                .updatedAt(status.getUpdatedAt())
                .lastUpdated(status.getUpdatedAt()) 
                .build();
    }

    public static StatusDto publicFromStatus(Status status) {
        return StatusDto.builder()
                .emoji(status.getEmoji())
                .message(status.getMessage())
                .isActive(status.getIsActive())
                .lastUpdated(status.getUpdatedAt())
                .build();
    }
} 