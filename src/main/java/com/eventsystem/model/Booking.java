package com.eventsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    private Item item;
    private Integer itemId;
    private LocalDateTime bookingTime;
    private LocalDateTime cancellationTime;
    private Integer totalPrice;
    private Status status;
    private boolean isCancelled;

    public enum Item {
        VENUE,
        OFFERING
    }

    public enum Status {
        CONFIRMED,
        CANCELLED,
        PENDING
    }
}