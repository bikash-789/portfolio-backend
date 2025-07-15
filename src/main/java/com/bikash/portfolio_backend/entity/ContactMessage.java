package com.bikash.portfolio_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_messages")
public class ContactMessage {

    @Id
    private String id;

    @TextIndexed
    private String name;

    @Indexed
    private String email;

    @TextIndexed
    private String subject;

    private String message;

    @Builder.Default
    private Status status = Status.UNREAD;

    private String ipAddress;

    private String userAgent;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum Status {
        UNREAD, READ, REPLIED, ARCHIVED
    }
} 