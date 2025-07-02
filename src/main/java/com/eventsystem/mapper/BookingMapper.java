package com.eventsystem.mapper;

import com.eventsystem.dto.BookingCreationDto;
import com.eventsystem.dto.BookingDto;
import com.eventsystem.dto.BookingUpdateDto;
import com.eventsystem.model.Booking;
import com.eventsystem.model.Event;
import com.eventsystem.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookingMapper {
    EventRepository eventRepository;
    public BookingDto toDto(Booking booking){
        return new BookingDto(
                booking.getEvent().getId(),
                booking.getItemId(),
                booking.getItem(),
                booking.getBookingTime(),
                booking.getTotalPrice(),
                booking.getCancellationTime(),
                booking.getStatus(),
                booking.isCancelled()
        );
    }

    public Booking toEntity(BookingCreationDto bookingCreationDto) {
        Event event = eventRepository.findById(bookingCreationDto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + bookingCreationDto.getEventId()));
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setItemId(bookingCreationDto.getItemId());
        booking.setItem(bookingCreationDto.getItem());
        booking.setBookingTime(bookingCreationDto.getBookingTime());
        booking.setTotalPrice(bookingCreationDto.getTotalPrice());
        booking.setStatus(Booking.Status.PENDING);
        booking.setCancelled(false);
        return booking;
    }

    public Booking updateFromDtoToEntity(BookingUpdateDto bookingUpdateDto, Booking booking){
        booking.setStatus(bookingUpdateDto.getStatus());
        booking.setTotalPrice(bookingUpdateDto.getTotalPrice());
        booking.setCancellationTime(bookingUpdateDto.getCancellationTime());
        booking.setCancelled(bookingUpdateDto.isCancelled());
        return booking;
    }
}
