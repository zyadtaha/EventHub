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
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    private Integer retailPrice;
    private LocalDateTime creationTime = LocalDateTime.now();

    public boolean canCancelWithoutPenalty() {
        LocalDateTime freeCancellationDeadline = creationTime.plusHours(48);
        return LocalDateTime.now().isBefore(freeCancellationDeadline);
    }

    public double calculateCancellationPenalty() {
        return canCancelWithoutPenalty() ? 0 : retailPrice * 0.2;
    }
}