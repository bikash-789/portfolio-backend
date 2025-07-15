package com.bikash.portfolio_backend.repository;

import com.bikash.portfolio_backend.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactMessageRepository extends MongoRepository<ContactMessage, String> {

    List<ContactMessage> findByStatus(ContactMessage.Status status);

    @Query("{'status': ?0}")
    Page<ContactMessage> findByStatusWithPagination(ContactMessage.Status status, Pageable pageable);

    @Query("{'$or': [{'name': {'$regex': ?0, '$options': 'i'}}, {'email': {'$regex': ?0, '$options': 'i'}}, {'subject': {'$regex': ?0, '$options': 'i'}}]}")
    Page<ContactMessage> findBySearchTerm(String searchTerm, Pageable pageable);

    @Query("{'$and': [{'$or': [{'name': {'$regex': ?0, '$options': 'i'}}, {'email': {'$regex': ?0, '$options': 'i'}}, {'subject': {'$regex': ?0, '$options': 'i'}}]}, {'status': ?1}]}")
    Page<ContactMessage> findBySearchTermAndStatus(String searchTerm, ContactMessage.Status status, Pageable pageable);

    @Query("{'createdAt': {$gte: ?0}}")
    List<ContactMessage> findMessagesCreatedAfter(LocalDateTime date);

    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<ContactMessage> findMessagesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{'status': ?0, 'createdAt': {$gte: ?1}}")
    List<ContactMessage> findByStatusAndCreatedAfter(ContactMessage.Status status, LocalDateTime date);

    @Query("{'email': ?0}")
    List<ContactMessage> findByEmail(String email);

    @Query("{'email': ?0, 'createdAt': {$gte: ?1}}")
    List<ContactMessage> findByEmailAndCreatedAfter(String email, LocalDateTime date);

    @Query("{'ipAddress': ?0, 'createdAt': {$gte: ?1}}")
    List<ContactMessage> findByIpAddressAndCreatedAfter(String ipAddress, LocalDateTime date);

    long countByStatus(ContactMessage.Status status);

    long countByCreatedAtAfter(LocalDateTime date);

    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
} 