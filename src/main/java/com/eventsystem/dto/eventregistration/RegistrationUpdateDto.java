package com.eventsystem.dto.eventregistration;

import com.eventsystem.model.EventRegistration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegistrationUpdateDto {
    private LocalDateTime cancellationTime;
    private boolean isCancelled;
    private EventRegistration.RegistrationStatus status;
}
