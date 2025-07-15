package com.bikash.portfolio_backend.dto.skill;

import com.bikash.portfolio_backend.entity.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSkillRequest {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must be less than 50 characters")
    private String category;

    @NotNull(message = "Skill level is required")
    private Skill.SkillLevel level;

    @Size(max = 500, message = "Icon URL must be less than 500 characters")
    private String icon;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Positive(message = "Years of experience must be positive")
    private Integer yearsOfExperience;

    @Builder.Default
    private Boolean featured = false;
} 