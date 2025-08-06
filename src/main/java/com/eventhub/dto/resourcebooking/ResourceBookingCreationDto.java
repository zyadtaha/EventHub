package com.eventhub.dto.resourcebooking;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResourceBookingCreationDto {
    private Long eventId;
    @Column(nullable = true)
    private Long venueId;
    @Column(nullable = true)
    private Long offeringId;
    private LocalDateTime bookingTime;
    private Integer totalPrice;
}
