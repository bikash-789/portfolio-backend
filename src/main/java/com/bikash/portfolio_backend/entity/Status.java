package com.bikash.portfolio_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_status")
public class Status {

    @Id
    private String id;

    @Field("user_id")
    @Indexed
    private String userId;

    @Field("emoji")
    private String emoji;

    @Field("message")
    private String message;

    @Field("is_active")
    @Indexed
    @Builder.Default
    private Boolean isActive = true;

    @Field("predefined_status_id")
    private String predefinedStatusId;

    @Field("expires_at")
    @Indexed
    private LocalDateTime expiresAt;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
} 