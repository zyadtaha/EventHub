package com.eventsystem.dto;

import com.eventsystem.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long eventId;
    private VenueDto venueDto;
    private OfferingDto offeringDto;
    private LocalDateTime bookingTime;
    private Integer totalPrice;
    private LocalDateTime cancellationTime;
    private Booking.Status status;
    private boolean isCancelled;
}
