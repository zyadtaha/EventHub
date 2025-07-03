package com.eventsystem.mapper;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.model.Booking;
import com.eventsystem.model.Event;
import com.eventsystem.repository.BookingRepository;
import com.eventsystem.service.OfferingService;
import com.eventsystem.service.VenueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EventMapper {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final OfferingService offeringService;
    private final VenueService venueService;

    public EventDto toDto(Event event) {
        return new EventDto(
                event.getName(),
                event.getDateTime(),
                event.getBookings().stream()
                        .map(bookingMapper::toDto)
                        .toList(),
                event.getRetailPrice(),
                event.getCreationTime(),
                event.canCancelWithoutPenalty(),
                event.calculateCancellationPenalty()
        );
    }

    public Event toEntity(EventCreationDto eventCreationDto) {
        Event event = new Event();
        event.setName(eventCreationDto.getName());
        event.setDateTime(eventCreationDto.getDateTime());
        event.setRetailPrice(eventCreationDto.getRetailPrice());
        return event;
    }

    public Event updateFromDtoToEntity(EventUpdateDto eventUpdateDto, Event event){
        event.setName(eventUpdateDto.getName());
        event.setDateTime(eventUpdateDto.getDateTime());
        event.setRetailPrice(eventUpdateDto.getRetailPrice());
        List<Booking> bookings = bookingRepository.findAllById(eventUpdateDto.getBookingIds());
        event.getBookings().clear();
        event.getBookings().addAll(bookings);
        return event;
    }
}
