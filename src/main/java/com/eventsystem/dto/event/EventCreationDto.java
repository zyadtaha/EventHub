package com.eventsystem.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventCreationDto {
    private String name;
    private Integer retailPrice;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}