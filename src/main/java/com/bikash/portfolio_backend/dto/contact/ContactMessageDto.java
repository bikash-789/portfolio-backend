package com.bikash.portfolio_backend.dto.contact;

import com.bikash.portfolio_backend.entity.ContactMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDto {

    private String id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private ContactMessage.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ipAddress;
    private String userAgent;
    private String notes;

    public static ContactMessageDto fromContactMessage(ContactMessage contactMessage) {
        return ContactMessageDto.builder()
                .id(contactMessage.getId())
                .name(contactMessage.getName())
                .email(contactMessage.getEmail())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .status(contactMessage.getStatus())
                .createdAt(contactMessage.getCreatedAt())
                .updatedAt(contactMessage.getUpdatedAt())
                .ipAddress(contactMessage.getIpAddress())
                .userAgent(contactMessage.getUserAgent())
                .notes(contactMessage.getNotes())
                .build();
    }
} 