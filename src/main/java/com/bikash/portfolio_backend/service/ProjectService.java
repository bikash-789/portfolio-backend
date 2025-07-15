package com.bikash.portfolio_backend.service;

import com.bikash.portfolio_backend.dto.project.CreateProjectRequest;
import com.bikash.portfolio_backend.dto.project.ProjectDto;
import com.bikash.portfolio_backend.dto.project.ProjectResponse;
import com.bikash.portfolio_backend.entity.Project;
import com.bikash.portfolio_backend.exception.ResourceNotFoundException;
import com.bikash.portfolio_backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse getAllProjects(int page, int limit, String category, Boolean featured, String search) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Project> projectPage;

        if (search != null && !search.trim().isEmpty()) {
            if (category != null && featured != null) {
                projectPage = projectRepository.findBySearchTermAndCategoryAndFeatured(search, category, featured, pageable);
            } else if (category != null) {
                projectPage = projectRepository.findBySearchTermAndCategory(search, category, pageable);
            } else if (featured != null) {
                projectPage = projectRepository.findBySearchTermAndFeatured(search, featured, pageable);
            } else {
                projectPage = projectRepository.findBySearchTerm(search, pageable);
            }
        } else {
            if (category != null && featured != null) {
                projectPage = projectRepository.findByCategoryAndFeatured(category, featured, pageable);
            } else if (category != null) {
                projectPage = projectRepository.findByCategoryWithPagination(category, pageable);
            } else if (featured != null) {
                projectPage = projectRepository.findByFeaturedWithPagination(featured, pageable);
            } else {
                projectPage = projectRepository.findAll(pageable);
            }
        }

        List<ProjectDto> projects = projectPage.getContent().stream()
                .map(ProjectDto::fromProject)
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .projects(projects)
                .total(projectPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(projectPage.getTotalPages())
                .build();
    }

    public ProjectDto getProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return ProjectDto.fromProject(project);
    }

    public ProjectDto getProjectBySlug(String slug) {
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with slug: " + slug));
        return ProjectDto.fromProject(project);
    }

    public ProjectDto createProject(CreateProjectRequest request) {
        if (projectRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Project with slug '" + request.getSlug() + "' already exists");
        }

        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .longDescription(request.getLongDescription())
                .image(request.getImage())
                .technologies(request.getTechnologies())
                .githubUrl(request.getGithubUrl())
                .liveUrl(request.getLiveUrl())
                .features(request.getFeatures())
                .slug(request.getSlug())
                .category(request.getCategory())
                .featured(request.isFeatured())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Project savedProject = projectRepository.save(project);
        return ProjectDto.fromProject(savedProject);
    }

    public ProjectDto updateProject(String id, CreateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // Check if slug is being changed and if it already exists
        if (!project.getSlug().equals(request.getSlug()) && projectRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Project with slug '" + request.getSlug() + "' already exists");
        }

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setLongDescription(request.getLongDescription());
        project.setImage(request.getImage());
        project.setTechnologies(request.getTechnologies());
        project.setGithubUrl(request.getGithubUrl());
        project.setLiveUrl(request.getLiveUrl());
        project.setFeatures(request.getFeatures());
        project.setSlug(request.getSlug());
        project.setCategory(request.getCategory());
        project.setFeatured(request.isFeatured());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        Project updatedProject = projectRepository.save(project);
        return ProjectDto.fromProject(updatedProject);
    }

    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    public List<ProjectDto> getFeaturedProjects() {
        List<Project> featuredProjects = projectRepository.findByFeaturedTrueOrderByCreatedAtDesc();
        return featuredProjects.stream()
                .map(ProjectDto::fromProject)
                .collect(Collectors.toList());
    }

    public ProjectResponse getProjectsByCategory(String category, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Project> projectPage = projectRepository.findByCategoryWithPagination(category, pageable);

        List<ProjectDto> projects = projectPage.getContent().stream()
                .map(ProjectDto::fromProject)
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .projects(projects)
                .total(projectPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(projectPage.getTotalPages())
                .build();
    }

    public ProjectResponse searchProjects(String query, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Project> projectPage = projectRepository.findBySearchTerm(query, pageable);

        List<ProjectDto> projects = projectPage.getContent().stream()
                .map(ProjectDto::fromProject)
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .projects(projects)
                .total(projectPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(projectPage.getTotalPages())
                .build();
    }
} 