package com.bikash.portfolio_backend.dto.status;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {

    private String emoji;

    @Size(max = 80, message = "Message cannot exceed 80 characters")
    private String message;

    private Boolean isActive;

    private String clearAfter; 
} 