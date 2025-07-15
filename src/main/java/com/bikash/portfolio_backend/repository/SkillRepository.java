package com.bikash.portfolio_backend.repository;

import com.bikash.portfolio_backend.entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends MongoRepository<Skill, String> {

    List<Skill> findByCategory(String category);
    List<Skill> findByCategoryIgnoreCase(String category);
    List<Skill> findByFeatured(boolean featured);
    List<Skill> findByLevel(Skill.SkillLevel level);
    List<Skill> findByCategoryAndLevel(String category, Skill.SkillLevel level);
    List<Skill> findByNameContainingIgnoreCase(String name);
    Optional<Skill> findByNameIgnoreCase(String name);
    List<Skill> findAllByOrderByNameAsc();
    List<Skill> findAllByOrderByCategoryAscNameAsc();
    List<Skill> findByCategoryOrderByNameAsc(String category);
    boolean existsByNameIgnoreCase(String name);
} 