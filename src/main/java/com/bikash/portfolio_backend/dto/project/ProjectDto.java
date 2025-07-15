package com.bikash.portfolio_backend.dto.project;

import com.bikash.portfolio_backend.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private String id;
    private String title;
    private String description;
    private String longDescription;
    private String image;
    private List<String> technologies;
    private String githubUrl;
    private String liveUrl;
    private List<String> features;
    private String slug;
    private String category;
    private boolean featured;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectDto fromProject(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .longDescription(project.getLongDescription())
                .image(project.getImage())
                .technologies(project.getTechnologies())
                .githubUrl(project.getGithubUrl())
                .liveUrl(project.getLiveUrl())
                .features(project.getFeatures())
                .slug(project.getSlug())
                .category(project.getCategory())
                .featured(project.isFeatured())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
} 