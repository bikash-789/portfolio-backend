package com.bikash.portfolio_backend.service;

import com.bikash.portfolio_backend.dto.contact.ContactFormRequest;
import com.bikash.portfolio_backend.dto.contact.ContactMessageDto;
import com.bikash.portfolio_backend.dto.contact.ContactResponse;
import com.bikash.portfolio_backend.dto.contact.ContactStatisticsDto;
import com.bikash.portfolio_backend.entity.ContactMessage;
import com.bikash.portfolio_backend.exception.ResourceNotFoundException;
import com.bikash.portfolio_backend.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String adminEmail;

    public ContactMessageDto submitContactForm(ContactFormRequest request) {
        ContactMessage contactMessage = ContactMessage.builder()
                .name(request.getName())
                .email(request.getEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(ContactMessage.Status.UNREAD)
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .build();

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        emailService.sendContactNotification(adminEmail, request.getName(), request.getEmail(), request.getSubject());

        return ContactMessageDto.fromContactMessage(savedMessage);
    }

    public ContactResponse getContactMessages(int page, int limit, ContactMessage.Status status, String search, String sortBy, String sortOrder) {
        Pageable pageable = createPageable(page, limit, sortBy, sortOrder);
        Page<ContactMessage> messagePage;

        if (search != null && !search.trim().isEmpty()) {
            if (status != null) {
                messagePage = contactMessageRepository.findBySearchTermAndStatus(search, status, pageable);
            } else {
                messagePage = contactMessageRepository.findBySearchTerm(search, pageable);
            }
        } else {
            if (status != null) {
                messagePage = contactMessageRepository.findByStatusWithPagination(status, pageable);
            } else {
                messagePage = contactMessageRepository.findAll(pageable);
            }
        }

        List<ContactMessageDto> messages = messagePage.getContent().stream()
                .map(ContactMessageDto::fromContactMessage)
                .collect(Collectors.toList());

        return ContactResponse.builder()
                .messages(messages)
                .total(messagePage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(messagePage.getTotalPages())
                .build();
    }

    public ContactMessageDto getContactMessageById(String id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));
        return ContactMessageDto.fromContactMessage(message);
    }

    public ContactMessageDto updateContactMessage(String id, Map<String, Object> updates) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));

        if (updates.containsKey("status")) {
            String statusStr = (String) updates.get("status");
            try {
                ContactMessage.Status status = ContactMessage.Status.valueOf(statusStr.toUpperCase());
                message.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + statusStr);
            }
        }

        if (updates.containsKey("notes")) {
            message.setNotes((String) updates.get("notes"));
        }

        ContactMessage updatedMessage = contactMessageRepository.save(message);
        return ContactMessageDto.fromContactMessage(updatedMessage);
    }

    public void deleteContactMessage(String id) {
        if (!contactMessageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact message not found with id: " + id);
        }
        contactMessageRepository.deleteById(id);
    }

    public ContactStatisticsDto getContactStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);
        LocalDateTime monthAgo = now.minusMonths(1);

        long total = contactMessageRepository.count();
        long unread = contactMessageRepository.countByStatus(ContactMessage.Status.UNREAD);
        long read = contactMessageRepository.countByStatus(ContactMessage.Status.READ);
        long replied = contactMessageRepository.countByStatus(ContactMessage.Status.REPLIED);
        long archived = contactMessageRepository.countByStatus(ContactMessage.Status.ARCHIVED);
        long thisWeek = contactMessageRepository.countByCreatedAtAfter(weekAgo);
        long thisMonth = contactMessageRepository.countByCreatedAtAfter(monthAgo);

        return ContactStatisticsDto.builder()
                .total(total)
                .unread(unread)
                .read(read)
                .replied(replied)
                .archived(archived)
                .thisWeek(thisWeek)
                .thisMonth(thisMonth)
                .build();
    }

    public ContactMessageDto markAsRead(String id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));

        message.setStatus(ContactMessage.Status.READ);
        ContactMessage updatedMessage = contactMessageRepository.save(message);
        return ContactMessageDto.fromContactMessage(updatedMessage);
    }

    public ContactMessageDto markAsReplied(String id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));

        message.setStatus(ContactMessage.Status.REPLIED);
        ContactMessage updatedMessage = contactMessageRepository.save(message);
        return ContactMessageDto.fromContactMessage(updatedMessage);
    }

    public ContactMessageDto archiveMessage(String id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact message not found with id: " + id));

        message.setStatus(ContactMessage.Status.ARCHIVED);
        ContactMessage updatedMessage = contactMessageRepository.save(message);
        return ContactMessageDto.fromContactMessage(updatedMessage);
    }

    private Pageable createPageable(int page, int limit, String sortBy, String sortOrder) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        String sortField = "createdAt"; 
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    sortField = "name";
                    break;
                case "email":
                    sortField = "email";
                    break;
                case "subject":
                    sortField = "subject";
                    break;
                case "updatedat":
                    sortField = "updatedAt";
                    break;
                default:
                    sortField = "createdAt";
            }
        }

        return PageRequest.of(page - 1, limit, Sort.by(direction, sortField));
    }
} 