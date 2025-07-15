package com.bikash.portfolio_backend.controller;

import com.bikash.portfolio_backend.dto.status.SetStatusRequest;
import com.bikash.portfolio_backend.dto.status.StatusDto;
import com.bikash.portfolio_backend.dto.status.UpdateStatusRequest;
import com.bikash.portfolio_backend.exception.ErrorResponse;
import com.bikash.portfolio_backend.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentStatus() {
        Optional<StatusDto> currentStatus = statusService.getCurrentStatus();
        
        if (currentStatus.isPresent()) {
            return ResponseEntity.ok(currentStatus.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.builder()
                            .error("No active status found")
                            .timestamp(LocalDateTime.now().toString())
                            .build());
        }
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMyStatus(Authentication authentication) {
        String userEmail = authentication.getName();
        Optional<StatusDto> myStatus = statusService.getMyStatus(userEmail);
        
        if (myStatus.isPresent()) {
            return ResponseEntity.ok(myStatus.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.builder()
                            .error("No status found for user")
                            .timestamp(LocalDateTime.now().toString())
                            .build());
        }
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusDto> setStatus(
            @Valid @RequestBody SetStatusRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        StatusDto status = statusService.setStatus(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    
    @PutMapping("/{statusId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusDto> updateStatus(
            @PathVariable String statusId,
            @Valid @RequestBody UpdateStatusRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        StatusDto status = statusService.updateStatus(statusId, userEmail, request);
        return ResponseEntity.ok(status);
    }


    @DeleteMapping("/current")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> clearCurrentStatus(Authentication authentication) {
        String userEmail = authentication.getName();
        statusService.clearCurrentStatus(userEmail);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StatusDto>> getStatusHistory(
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        String userEmail = authentication.getName();
        
        // Enforce maximum limit
        int actualLimit = Math.min(Math.max(limit, 1), 50);
        
        List<StatusDto> history = statusService.getStatusHistory(userEmail, actualLimit);
        return ResponseEntity.ok(history);
    }
} 