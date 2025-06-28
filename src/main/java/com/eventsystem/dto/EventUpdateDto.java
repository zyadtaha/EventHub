package com.eventsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventUpdateDto {
    private String name;
    private LocalDateTime dateTime;
    private Long venueId;
    private Integer retailPrice;
}
