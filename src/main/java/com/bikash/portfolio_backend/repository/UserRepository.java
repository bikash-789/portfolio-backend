package com.bikash.portfolio_backend.repository;

import com.bikash.portfolio_backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{'emailVerificationToken': ?0, 'emailVerificationExpiry': {$gt: ?1}}")
    Optional<User> findByEmailVerificationTokenAndExpiryAfter(String token, LocalDateTime now);

    @Query("{'passwordResetToken': ?0, 'passwordResetExpiry': {$gt: ?1}}")
    Optional<User> findByPasswordResetTokenAndExpiryAfter(String token, LocalDateTime now);

    @Query("{'refreshToken': ?0}")
    Optional<User> findByRefreshToken(String refreshToken);

    List<User> findByRole(User.Role role);

    @Query("{'createdAt': {$gte: ?0}}")
    List<User> findUsersCreatedAfter(LocalDateTime date);

    @Query("{'emailVerified': ?0}")
    List<User> findByEmailVerified(boolean emailVerified);

    @Query("{'email': {$regex: ?0, $options: 'i'}}")
    List<User> findByEmailContainingIgnoreCase(String email);

    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<User> findByNameContainingIgnoreCase(String name);
} 