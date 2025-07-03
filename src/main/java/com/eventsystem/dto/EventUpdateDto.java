package com.eventsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class EventUpdateDto {
    private String name;
    private LocalDateTime dateTime;
    private Integer retailPrice;
    private List<Long> bookingIds;
}