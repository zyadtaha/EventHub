package com.eventhub.model;

import com.eventhub.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer retailPrice;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    public enum EventType {
        WEDDING, ENGAGEMENT_PARTY, BIRTHDAY,
        CONFERENCE, WORKSHOP, SEMINAR
    }

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResourceBooking> resourceBookings = new ArrayList<>();

    private LocalDateTime cancellationTime;
    private boolean isCancelled;

    @Override
    public String toString(){
        return "Event{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                ", organizerId='" + super.getCreatedBy() + '\'' +
                ", retailPrice=" + retailPrice +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", creationTime=" + super.getCreatedAt() +
                ", cancellationTime=" + cancellationTime +
                ", isCancelled=" + isCancelled +
                ", type=" + type +
                '}';
    }

    public boolean canCancelWithoutPenalty() {
        LocalDateTime freeCancellationDeadline = super.getCreatedAt().plusHours(48);
        return LocalDateTime.now().isBefore(freeCancellationDeadline);
    }

    public double calculateCancellationPenalty() {
        return canCancelWithoutPenalty() ? 0 : retailPrice * 0.2;
    }
}