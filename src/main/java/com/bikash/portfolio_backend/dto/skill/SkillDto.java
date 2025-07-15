package com.bikash.portfolio_backend.dto.skill;

import com.bikash.portfolio_backend.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {

    private String id;
    private String name;
    private String category;
    private Skill.SkillLevel level;
    private String icon;
    private String description;
    private Integer yearsOfExperience;
    private boolean featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SkillDto fromSkill(Skill skill) {
        return SkillDto.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .level(skill.getLevel())
                .icon(skill.getIcon())
                .description(skill.getDescription())
                .yearsOfExperience(skill.getYearsOfExperience())
                .featured(skill.isFeatured())
                .createdAt(skill.getCreatedAt())
                .updatedAt(skill.getUpdatedAt())
                .build();
    }
} 