package com.eventhub.dto.eventregistration;

import com.eventhub.model.EventRegistration;
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
