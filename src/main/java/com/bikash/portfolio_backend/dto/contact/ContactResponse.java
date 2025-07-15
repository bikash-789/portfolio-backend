package com.bikash.portfolio_backend.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {

    private List<ContactMessageDto> messages;
    private long total;
    private int page;
    private int limit;
    private int totalPages;
} 