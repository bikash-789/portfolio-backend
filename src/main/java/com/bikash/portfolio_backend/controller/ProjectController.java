package com.bikash.portfolio_backend.controller;

import com.bikash.portfolio_backend.dto.project.CreateProjectRequest;
import com.bikash.portfolio_backend.dto.project.ProjectDto;
import com.bikash.portfolio_backend.dto.project.ProjectResponse;
import com.bikash.portfolio_backend.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management APIs")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves paginated list of projects with optional filtering")
    public ResponseEntity<ProjectResponse> getAllProjects(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
            @Parameter(description = "Filter by featured status") @RequestParam(required = false) Boolean featured,
            @Parameter(description = "Search term") @RequestParam(required = false) String search) {
        
        ProjectResponse response = projectService.getAllProjects(page, limit, category, featured, search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieves a specific project by its ID")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable String id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get project by slug", description = "Retrieves a specific project by its slug")
    public ResponseEntity<ProjectDto> getProjectBySlug(@PathVariable String slug) {
        ProjectDto project = projectService.getProjectBySlug(slug);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create project", description = "Creates a new project (Admin only)")
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectDto project = projectService.createProject(request);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update project", description = "Updates an existing project (Admin only)")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable String id,
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectDto project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete project", description = "Deletes a project (Admin only)")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured projects", description = "Retrieves all featured projects")
    public ResponseEntity<List<ProjectDto>> getFeaturedProjects() {
        List<ProjectDto> projects = projectService.getFeaturedProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get projects by category", description = "Retrieves paginated projects by category")
    public ResponseEntity<ProjectResponse> getProjectsByCategory(
            @PathVariable String category,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int limit) {
        
        ProjectResponse response = projectService.getProjectsByCategory(category, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search projects", description = "Searches projects by title, description, or technologies")
    public ResponseEntity<ProjectResponse> searchProjects(
            @Parameter(description = "Search query") @RequestParam String q,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int limit) {
        
        ProjectResponse response = projectService.searchProjects(q, page, limit);
        return ResponseEntity.ok(response);
    }
} 