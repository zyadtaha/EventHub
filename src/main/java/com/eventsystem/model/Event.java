package com.eventsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, updatable = false)
    private String organizerId;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResourceBooking> resourceBookings = new ArrayList<>();

    @Column(nullable = false)
    private Integer retailPrice;

    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private LocalDateTime creationTime = LocalDateTime.now();
    private LocalDateTime cancellationTime;
    private boolean isCancelled = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    public enum EventType {
        WEDDING, ENGAGEMENT_PARTY, BIRTHDAY,
        CONFERENCE, WORKSHOP, SEMINAR
    }

    public boolean canCancelWithoutPenalty() {
        LocalDateTime freeCancellationDeadline = creationTime.plusHours(48);
        return LocalDateTime.now().isBefore(freeCancellationDeadline);
    }

    public double calculateCancellationPenalty() {
        return canCancelWithoutPenalty() ? 0 : retailPrice * 0.2;
    }
}