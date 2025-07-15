package com.bikash.portfolio_backend.controller;

import com.bikash.portfolio_backend.dto.contact.ContactFormRequest;
import com.bikash.portfolio_backend.dto.contact.ContactMessageDto;
import com.bikash.portfolio_backend.dto.contact.ContactResponse;
import com.bikash.portfolio_backend.dto.contact.ContactStatisticsDto;
import com.bikash.portfolio_backend.entity.ContactMessage;
import com.bikash.portfolio_backend.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "Contact form and message management APIs")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Submit contact form", description = "Submits a new contact form message")
    public ResponseEntity<ContactMessageDto> submitContactForm(
            @Valid @RequestBody ContactFormRequest request,
            HttpServletRequest httpRequest) {
        
        // Extract IP address and user agent
        String ipAddress = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        request.setIpAddress(ipAddress);
        request.setUserAgent(userAgent);
        
        ContactMessageDto message = contactService.submitContactForm(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get contact messages", description = "Retrieves paginated list of contact messages (Admin only)")
    public ResponseEntity<ContactResponse> getContactMessages(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filter by status") @RequestParam(required = false) ContactMessage.Status status,
            @Parameter(description = "Search term") @RequestParam(required = false) String search,
            @Parameter(description = "Sort by field") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort order (asc/desc)") @RequestParam(required = false) String sortOrder) {
        
        ContactResponse response = contactService.getContactMessages(page, limit, status, search, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get contact message by ID", description = "Retrieves a specific contact message (Admin only)")
    public ResponseEntity<ContactMessageDto> getContactMessageById(@PathVariable String id) {
        ContactMessageDto message = contactService.getContactMessageById(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update contact message", description = "Updates a contact message status and notes (Admin only)")
    public ResponseEntity<ContactMessageDto> updateContactMessage(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        ContactMessageDto message = contactService.updateContactMessage(id, updates);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete contact message", description = "Deletes a contact message (Admin only)")
    public ResponseEntity<Map<String, String>> deleteContactMessage(@PathVariable String id) {
        contactService.deleteContactMessage(id);
        return ResponseEntity.ok(Map.of("message", "Contact message deleted successfully"));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get contact statistics", description = "Retrieves contact message statistics (Admin only)")
    public ResponseEntity<ContactStatisticsDto> getContactStatistics() {
        ContactStatisticsDto stats = contactService.getContactStatistics();
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mark message as read", description = "Marks a contact message as read (Admin only)")
    public ResponseEntity<ContactMessageDto> markAsRead(@PathVariable String id) {
        ContactMessageDto message = contactService.markAsRead(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}/replied")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mark message as replied", description = "Marks a contact message as replied (Admin only)")
    public ResponseEntity<ContactMessageDto> markAsReplied(@PathVariable String id) {
        ContactMessageDto message = contactService.markAsReplied(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Archive message", description = "Archives a contact message (Admin only)")
    public ResponseEntity<ContactMessageDto> archiveMessage(@PathVariable String id) {
        ContactMessageDto message = contactService.archiveMessage(id);
        return ResponseEntity.ok(message);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 