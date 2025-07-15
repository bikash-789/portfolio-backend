package com.bikash.portfolio_backend.repository;

import com.bikash.portfolio_backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    Optional<Project> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Project> findByFeaturedTrue();

    List<Project> findByCategory(String category);

    @Query("{'$text': {'$search': ?0}}")
    Page<Project> findByTextSearch(String searchTerm, Pageable pageable);

    @Query("{'$or': [{'title': {'$regex': ?0, '$options': 'i'}}, {'description': {'$regex': ?0, '$options': 'i'}}, {'technologies': {'$regex': ?0, '$options': 'i'}}]}")
    Page<Project> findBySearchTerm(String searchTerm, Pageable pageable);

    @Query("{'category': ?0}")
    Page<Project> findByCategoryWithPagination(String category, Pageable pageable);

    @Query("{'featured': ?0}")
    Page<Project> findByFeaturedWithPagination(boolean featured, Pageable pageable);

    @Query("{'$and': [{'category': ?0}, {'featured': ?1}]}")
    Page<Project> findByCategoryAndFeatured(String category, boolean featured, Pageable pageable);

    @Query("{'$and': [{'$or': [{'title': {'$regex': ?0, '$options': 'i'}}, {'description': {'$regex': ?0, '$options': 'i'}}, {'technologies': {'$regex': ?0, '$options': 'i'}}]}, {'category': ?1}]}")
    Page<Project> findBySearchTermAndCategory(String searchTerm, String category, Pageable pageable);

    @Query("{'$and': [{'$or': [{'title': {'$regex': ?0, '$options': 'i'}}, {'description': {'$regex': ?0, '$options': 'i'}}, {'technologies': {'$regex': ?0, '$options': 'i'}}]}, {'featured': ?1}]}")
    Page<Project> findBySearchTermAndFeatured(String searchTerm, boolean featured, Pageable pageable);

    @Query("{'$and': [{'$or': [{'title': {'$regex': ?0, '$options': 'i'}}, {'description': {'$regex': ?0, '$options': 'i'}}, {'technologies': {'$regex': ?0, '$options': 'i'}}]}, {'category': ?1}, {'featured': ?2}]}")
    Page<Project> findBySearchTermAndCategoryAndFeatured(String searchTerm, String category, boolean featured, Pageable pageable);

    List<Project> findAllByOrderByCreatedAtDesc();

    List<Project> findByCategoryOrderByCreatedAtDesc(String category);

    List<Project> findByFeaturedTrueOrderByCreatedAtDesc();
} 