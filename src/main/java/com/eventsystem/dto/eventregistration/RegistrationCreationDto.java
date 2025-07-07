package com.eventsystem.dto.eventregistration;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationCreationDto {
    @Column(nullable = false, updatable = false)
    private Long eventId;
}
