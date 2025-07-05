package com.eventsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = true)
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "offering_id", nullable = true)
    private Offering offering;

    @Column(nullable = false, updatable = false)
    private String organizerId;

    @Column(nullable = false, updatable = false)
    private String providerId;

    private LocalDateTime bookingTime;
    private LocalDateTime cancellationTime;
    private Integer totalPrice;
    private Status status;
    private boolean isCancelled;

    public enum Status {
        CONFIRMED,
        CANCELLED,
        PENDING
    }
}