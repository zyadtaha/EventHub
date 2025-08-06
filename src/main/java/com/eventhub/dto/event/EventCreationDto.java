package com.eventhub.dto.event;

import com.eventhub.model.Event.EventType;
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
    private EventType type;
}