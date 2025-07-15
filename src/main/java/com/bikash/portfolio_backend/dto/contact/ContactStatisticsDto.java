package com.bikash.portfolio_backend.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactStatisticsDto {

    private long total;
    private long unread;
    private long read;
    private long replied;
    private long archived;
    private long thisWeek;
    private long thisMonth;
} 