package com.eventsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventDto {
    private String name;
    private LocalDateTime dateTime;
    private VenueDto venuDto;
    private Integer retailPrice;
    private LocalDateTime creationTime;
    private boolean canCancelWithoutPenalty;
    private Double cancellationPenalty;
}
