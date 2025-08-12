package com.eventhub.model;

import com.eventhub.common.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@RequiredArgsConstructor
public class ResourceBooking extends BaseEntity {
    // TODO: Do you really need event, offering, venue ? you already have their IDs,
    // TODO: Think of having a super class called bookable ?
    @ManyToOne
    private Event event;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "offering_id")
    private Offering offering;

    @Column(nullable = false, updatable = false)
    private String providerId;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime cancellationTime;
    private boolean isCancelled;

    public enum Status {
        CONFIRMED, CANCELLED, PENDING
    }
}