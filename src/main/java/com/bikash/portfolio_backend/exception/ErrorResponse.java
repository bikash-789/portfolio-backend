package com.bikash.portfolio_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String error;
    private String message;
    private String timestamp;
    private String path;
    private Map<String, String> details;
} 