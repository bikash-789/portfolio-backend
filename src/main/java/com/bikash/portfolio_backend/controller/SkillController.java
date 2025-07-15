package com.bikash.portfolio_backend.controller;

import com.bikash.portfolio_backend.dto.skill.CreateSkillRequest;
import com.bikash.portfolio_backend.dto.skill.SkillDto;
import com.bikash.portfolio_backend.dto.skill.SkillResponse;
import com.bikash.portfolio_backend.dto.skill.UpdateSkillRequest;
import com.bikash.portfolio_backend.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Skills management APIs")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    @Operation(summary = "Get all skills", description = "Retrieves all skills ordered by category and name")
    public ResponseEntity<SkillResponse> getAllSkills() {
        List<SkillDto> skills = skillService.getAllSkills();
        SkillResponse response = SkillResponse.success(skills, "Skills retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get skills by category", description = "Retrieves skills filtered by category")
    public ResponseEntity<SkillResponse> getSkillsByCategory(@PathVariable String category) {
        List<SkillDto> skills = skillService.getSkillsByCategory(category);
        SkillResponse response = SkillResponse.success(skills, "Skills retrieved successfully", category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured skills", description = "Retrieves only featured skills")
    public ResponseEntity<SkillResponse> getFeaturedSkills() {
        List<SkillDto> skills = skillService.getFeaturedSkills();
        SkillResponse response = SkillResponse.success(skills, "Featured skills retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieves all unique skill categories")
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        List<String> categories = skillService.getAllCategories();
        return ResponseEntity.ok(Map.of(
            "categories", categories,
            "total", categories.size(),
            "message", "Categories retrieved successfully"
        ));
    }

    @GetMapping("/search")
    @Operation(summary = "Search skills", description = "Search skills by name")
    public ResponseEntity<SkillResponse> searchSkills(@RequestParam String query) {
        List<SkillDto> skills = skillService.searchSkills(query);
        SkillResponse response = SkillResponse.success(skills, "Search results retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get skill by ID", description = "Retrieves a specific skill by ID")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable String id) {
        SkillDto skill = skillService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new skill", description = "Creates a new skill (admin only)")
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody CreateSkillRequest request) {
        SkillDto skill = skillService.createSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update skill", description = "Updates an existing skill (admin only)")
    public ResponseEntity<SkillDto> updateSkill(@PathVariable String id, @Valid @RequestBody UpdateSkillRequest request) {
        SkillDto skill = skillService.updateSkill(id, request);
        return ResponseEntity.ok(skill);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete skill", description = "Deletes a skill (admin only)")
    public ResponseEntity<Map<String, String>> deleteSkill(@PathVariable String id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(Map.of("message", "Skill deleted successfully"));
    }
} 