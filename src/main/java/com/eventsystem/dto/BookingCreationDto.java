package com.eventsystem.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreationDto {
    private Long eventId;
    @Column(nullable = true)
    private Long venueId;
    @Column(nullable = true)
    private Long offeringId;
    private LocalDateTime bookingTime;
    private Integer totalPrice;
}
