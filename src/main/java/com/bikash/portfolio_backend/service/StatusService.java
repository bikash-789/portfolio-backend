package com.bikash.portfolio_backend.service;

import com.bikash.portfolio_backend.dto.status.SetStatusRequest;
import com.bikash.portfolio_backend.dto.status.StatusDto;
import com.bikash.portfolio_backend.dto.status.UpdateStatusRequest;
import com.bikash.portfolio_backend.entity.Status;
import com.bikash.portfolio_backend.exception.ResourceNotFoundException;
import com.bikash.portfolio_backend.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private final StatusRepository statusRepository;

    public Optional<StatusDto> getCurrentStatus() {
        deactivateExpiredStatuses();
        return statusRepository.findActiveStatus()
                .map(StatusDto::publicFromStatus);
    }

    public Optional<StatusDto> getMyStatus(String userId) {
        deactivateExpiredStatuses();
        return statusRepository.findActiveStatusByUserId(userId)
                .map(StatusDto::fromStatus);
    }

    @Transactional
    public StatusDto setStatus(String userId, SetStatusRequest request) {
        deactivateUserActiveStatuses(userId);

        LocalDateTime expiresAt = calculateExpirationTime(request.getClearAfter());

        Status status = Status.builder()
                .userId(userId)
                .emoji(request.getEmoji().trim())
                .message(request.getMessage().trim())
                .predefinedStatusId(request.getPredefinedStatusId())
                .isActive(true)
                .expiresAt(expiresAt)
                .build();

        Status savedStatus = statusRepository.save(status);
        log.info("Status set for user {}: {}", userId, savedStatus.getMessage());

        return StatusDto.fromStatus(savedStatus);
    }

    @Transactional
    public StatusDto updateStatus(String statusId, String userId, UpdateStatusRequest request) {
        Status status = statusRepository.findByIdAndUserId(statusId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with id: " + statusId));

        if (request.getEmoji() != null) {
            status.setEmoji(request.getEmoji().trim());
        }
        if (request.getMessage() != null) {
            status.setMessage(request.getMessage().trim());
        }
        if (request.getIsActive() != null) {
            status.setIsActive(request.getIsActive());
        }
        if (request.getClearAfter() != null) {
            status.setExpiresAt(calculateExpirationTime(request.getClearAfter()));
        }

        Status updatedStatus = statusRepository.save(status);
        log.info("Status updated for user {}: {}", userId, updatedStatus.getId());

        return StatusDto.fromStatus(updatedStatus);
    }

    @Transactional
    public void clearCurrentStatus(String userId) {
        deactivateUserActiveStatuses(userId);
        log.info("Status cleared for user {}", userId);
    }

    public List<StatusDto> getStatusHistory(String userId, int limit) {
        Pageable pageable = PageRequest.of(0, Math.min(limit, 50), 
                Sort.by(Sort.Direction.DESC, "createdAt"));
        
        return statusRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .getContent()
                .stream()
                .map(StatusDto::fromStatus)
                .collect(Collectors.toList());
    }

    private LocalDateTime calculateExpirationTime(String clearAfter) {
        if (clearAfter == null || "never".equals(clearAfter)) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();

        switch (clearAfter.toLowerCase()) {
            case "today":
                return now.toLocalDate().atTime(LocalTime.MAX);
            case "week":
                return now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                          .toLocalDate().atTime(LocalTime.MAX);
            default:
                try {
                    int minutes = Integer.parseInt(clearAfter);
                    return now.plusMinutes(minutes);
                } catch (NumberFormatException e) {
                    log.warn("Invalid clearAfter value: {}, defaulting to never expire", clearAfter);
                    return null;
                }
        }
    }

    @Transactional
    public void deactivateUserActiveStatuses(String userId) {
        List<Status> activeStatuses = statusRepository.findActiveStatusesByUserId(userId);
        activeStatuses.forEach(status -> status.setIsActive(false));
        if (!activeStatuses.isEmpty()) {
            statusRepository.saveAll(activeStatuses);
        }
    }

    @Scheduled(fixedRate = 60000) 
    @Transactional
    public void deactivateExpiredStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Status> expiredStatuses = statusRepository.findExpiredActiveStatuses(now);
        
        if (!expiredStatuses.isEmpty()) {
            expiredStatuses.forEach(status -> status.setIsActive(false));
            statusRepository.saveAll(expiredStatuses);
            log.info("Deactivated {} expired statuses", expiredStatuses.size());
        }
    }

    @Scheduled(cron = "0 0 17 L * ?", zone="Asia/Kolkata")
    @Transactional
    public void cleanStatusHistory() {
        statusRepository.deleteAll();
        LocalDateTime now = LocalDateTime.now();
        String monthName = now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        log.info("Deleted status history for {} month", monthName);
    }
} 