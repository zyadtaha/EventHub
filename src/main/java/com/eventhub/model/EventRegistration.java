package com.eventhub.model;

import com.eventhub.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@RequiredArgsConstructor
public class EventRegistration extends BaseEntity {
    // TODO: Should it be a String (as well as other IDs)?
    @Column(nullable = false, updatable = false)
    private Long eventId;
    @Column(nullable = false, updatable = false)
    private String attendeeName;
    @Column(nullable = false, updatable = false)
    private String attendeeEmail;
    @Column(nullable = false, updatable = false)
    private String organizerId;

    private LocalDateTime cancellationTime;
    private boolean isCancelled;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    public enum RegistrationStatus {
        CONFIRMED, CANCELLED, WAITLISTED
    }
}