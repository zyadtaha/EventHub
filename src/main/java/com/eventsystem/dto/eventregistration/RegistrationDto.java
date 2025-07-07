package com.eventsystem.dto.eventregistration;

import com.eventsystem.model.EventRegistration;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDto {
    private String eventName;
    private String attendeeName;
    private Integer price;
    private EventRegistration.RegistrationStatus status;
}
