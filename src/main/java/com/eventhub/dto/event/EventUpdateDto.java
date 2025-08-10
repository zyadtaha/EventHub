package com.eventhub.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class EventUpdateDto {
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer retailPrice;
    // TODO: should be addition to the existing bookings, not a replacement
    private List<Long> bookingIds;
}