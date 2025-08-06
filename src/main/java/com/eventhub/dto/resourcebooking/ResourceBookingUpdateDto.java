package com.eventhub.dto.resourcebooking;

import com.eventhub.model.ResourceBooking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResourceBookingUpdateDto {
    private LocalDateTime cancellationTime;
    private ResourceBooking.Status status;
    private boolean isCancelled;
}