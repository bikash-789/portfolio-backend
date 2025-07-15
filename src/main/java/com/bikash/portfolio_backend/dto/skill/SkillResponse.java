package com.bikash.portfolio_backend.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {

    private List<SkillDto> skills;
    private String message;
    private int total;
    private String category;

    public static SkillResponse success(List<SkillDto> skills, String message) {
        return SkillResponse.builder()
                .skills(skills)
                .message(message)
                .total(skills.size())
                .build();
    }

    public static SkillResponse success(List<SkillDto> skills, String message, String category) {
        return SkillResponse.builder()
                .skills(skills)
                .message(message)
                .total(skills.size())
                .category(category)
                .build();
    }
} 