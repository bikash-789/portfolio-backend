package com.bikash.portfolio_backend.repository;

import com.bikash.portfolio_backend.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends MongoRepository<Status, String> {

    @Query("{'isActive': true}")
    Optional<Status> findActiveStatus();

    @Query("{'userId': ?0, 'isActive': true}")
    Optional<Status> findActiveStatusByUserId(String userId);

    @Query("{'userId': ?0}")
    Page<Status> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    @Query("{'userId': ?0}")
    List<Status> findByUserIdOrderByCreatedAtDesc(String userId);

    @Query("{'isActive': true, 'expiresAt': {$lte: ?0}}")
    List<Status> findExpiredActiveStatuses(LocalDateTime now);

    @Query("{'userId': ?0, 'isActive': true}")
    List<Status> findActiveStatusesByUserId(String userId);

    @Query("{'_id': ?0, 'userId': ?1}")
    Optional<Status> findByIdAndUserId(String id, String userId);

    boolean existsByUserIdAndIsActive(String userId, boolean isActive);
} 