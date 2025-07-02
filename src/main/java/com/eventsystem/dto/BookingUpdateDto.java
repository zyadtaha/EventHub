package com.eventsystem.dto;

import com.eventsystem.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingUpdateDto {
    private Integer totalPrice;
    private LocalDateTime cancellationTime;
    private Booking.Status status;
    private boolean isCancelled;
}