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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projects")
public class Project {

    @Id
    private String id;

    @TextIndexed
    private String title;

    private String description;

    private String longDescription;

    private String image;

    private List<String> technologies;

    private String githubUrl;

    private String liveUrl;

    private List<String> features;

    @Indexed(unique = true)
    private String slug;

    private String category;

    @Builder.Default
    private boolean featured = false;

    private LocalDate startDate;

    private LocalDate endDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
} 