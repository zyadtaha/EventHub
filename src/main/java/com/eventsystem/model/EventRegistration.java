package com.eventsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
public class EventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Should it be a String (as well as other IDs)?
    @Column(nullable = false, updatable = false)
    private Long eventId;

    @Column(nullable = false, updatable = false)
    private String attendeeId;
    private String attendeeName;
    private String attendeeEmail;

    @Column(nullable = false, updatable = false)
    private String organizerId;

    private LocalDateTime registrationDate = LocalDateTime.now();
    private LocalDateTime cancellationTime;
    private boolean isCancelled;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    public enum RegistrationStatus {
        CONFIRMED, CANCELLED, WAITLISTED
    }
}