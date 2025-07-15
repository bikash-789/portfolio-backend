package com.bikash.portfolio_backend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private List<ProjectDto> projects;
    private long total;
    private int page;
    private int limit;
    private int totalPages;
} 