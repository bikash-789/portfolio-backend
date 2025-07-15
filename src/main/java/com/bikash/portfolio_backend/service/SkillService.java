package com.bikash.portfolio_backend.service;

import com.bikash.portfolio_backend.dto.skill.CreateSkillRequest;
import com.bikash.portfolio_backend.dto.skill.SkillDto;
import com.bikash.portfolio_backend.dto.skill.UpdateSkillRequest;
import com.bikash.portfolio_backend.entity.Skill;
import com.bikash.portfolio_backend.exception.AuthenticationException;
import com.bikash.portfolio_backend.exception.ResourceNotFoundException;
import com.bikash.portfolio_backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<SkillDto> getAllSkills() {
        List<Skill> skills = skillRepository.findAllByOrderByCategoryAscNameAsc();
        return skills.stream()
                .map(SkillDto::fromSkill)
                .collect(Collectors.toList());
    }

    public List<SkillDto> getSkillsByCategory(String category) {
        List<Skill> skills = skillRepository.findByCategoryOrderByNameAsc(category);
        return skills.stream()
                .map(SkillDto::fromSkill)
                .collect(Collectors.toList());
    }

    public List<SkillDto> getFeaturedSkills() {
        List<Skill> skills = skillRepository.findByFeatured(true);
        return skills.stream()
                .map(SkillDto::fromSkill)
                .collect(Collectors.toList());
    }

    public SkillDto getSkillById(String id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        return SkillDto.fromSkill(skill);
    }

    public SkillDto createSkill(CreateSkillRequest request) {
        // Check if skill with same name already exists
        if (skillRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AuthenticationException("Skill with name '" + request.getName() + "' already exists");
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .level(request.getLevel())
                .icon(request.getIcon())
                .description(request.getDescription())
                .yearsOfExperience(request.getYearsOfExperience())
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .build();

        Skill savedSkill = skillRepository.save(skill);
        return SkillDto.fromSkill(savedSkill);
    }

    public SkillDto updateSkill(String id, UpdateSkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));

        // Check if another skill with same name exists
        if (!skill.getName().equalsIgnoreCase(request.getName()) && 
            skillRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AuthenticationException("Skill with name '" + request.getName() + "' already exists");
        }

        skill.setName(request.getName());
        skill.setCategory(request.getCategory());
        skill.setLevel(request.getLevel());
        skill.setIcon(request.getIcon());
        skill.setDescription(request.getDescription());
        skill.setYearsOfExperience(request.getYearsOfExperience());
        
        if (request.getFeatured() != null) {
            skill.setFeatured(request.getFeatured());
        }

        Skill updatedSkill = skillRepository.save(skill);
        return SkillDto.fromSkill(updatedSkill);
    }

    public void deleteSkill(String id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        
        skillRepository.delete(skill);
    }

    public List<String> getAllCategories() {
        return skillRepository.findAll().stream()
                .map(Skill::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<SkillDto> searchSkills(String query) {
        List<Skill> skills = skillRepository.findByNameContainingIgnoreCase(query);
        return skills.stream()
                .map(SkillDto::fromSkill)
                .collect(Collectors.toList());
    }
} 