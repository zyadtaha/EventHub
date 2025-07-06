package com.eventsystem.dto.resourcebooking;

import com.eventsystem.dto.OfferingDto;
import com.eventsystem.dto.VenueDto;
import com.eventsystem.model.ResourceBooking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResourceBookingDto {
    // TODO: make it EventDto
    private Long eventId;
    private VenueDto venueDto;
    private OfferingDto offeringDto;
    private LocalDateTime bookingTime;
    private Integer totalPrice;
    private LocalDateTime cancellationTime;
    private ResourceBooking.Status status;
    private boolean isCancelled;
}
