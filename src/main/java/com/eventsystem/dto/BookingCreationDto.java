package com.eventsystem.dto;

import com.eventsystem.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreationDto {
    private Long eventId;
    private Integer itemId;
    private Booking.Item item;
    private LocalDateTime bookingTime;
    private Integer totalPrice;
}
