package com.eventsystem.mapper;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.model.ResourceBooking;
import com.eventsystem.model.Event;
import com.eventsystem.repository.ResourceBookingRepository;
import com.eventsystem.service.OfferingService;
import com.eventsystem.service.VenueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EventMapper {
    private final ResourceBookingRepository resourceBookingRepository;
    private final ResourceBookingMapper resourceBookingMapper;
    private final OfferingService offeringService;
    private final VenueService venueService;

    public EventDto toDto(Event event) {
        return new EventDto(
                event.getName(),
                event.getDateTime(),
                event.getResourceBookings().stream()
                        .map(resourceBookingMapper::toDto)
                        .toList(),
                event.getRetailPrice(),
                event.getCreationTime(),
                event.canCancelWithoutPenalty(),
                event.calculateCancellationPenalty()
        );
    }

    public Event toEntity(EventCreationDto eventCreationDto, String organizerId) {
        Event event = new Event();
        event.setName(eventCreationDto.getName());
        event.setDateTime(eventCreationDto.getDateTime());
        event.setRetailPrice(eventCreationDto.getRetailPrice());
        event.setOrganizerId(organizerId);
        return event;
    }

    public Event updateFromDtoToEntity(EventUpdateDto eventUpdateDto, Event event, String organizerId){
        event.setName(eventUpdateDto.getName());
        event.setDateTime(eventUpdateDto.getDateTime());
        event.setRetailPrice(eventUpdateDto.getRetailPrice());
        event.setOrganizerId(organizerId);
        List<ResourceBooking> resourceBookings = resourceBookingRepository.findAllById(eventUpdateDto.getBookingIds());
        event.getResourceBookings().clear();
        event.getResourceBookings().addAll(resourceBookings);
        return event;
    }
}
