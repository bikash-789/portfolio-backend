package com.bikash.portfolio_backend.dto.status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetStatusRequest {

    @NotBlank(message = "Emoji is required")
    private String emoji;

    @NotBlank(message = "Message is required")
    @Size(max = 80, message = "Message cannot exceed 80 characters")
    private String message;

    private String predefinedStatusId;

    private String clearAfter; 
} 