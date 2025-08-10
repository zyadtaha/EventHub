package com.eventhub.mapper;

import com.eventhub.dto.event.EventCreationDto;
import com.eventhub.dto.event.EventDto;
import com.eventhub.dto.event.EventUpdateDto;
import com.eventhub.model.ResourceBooking;
import com.eventhub.model.Event;
import com.eventhub.repository.ResourceBookingRepository;
import com.eventhub.service.OfferingService;
import com.eventhub.service.VenueService;
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
                event.getStartDateTime(),
                event.getEndDateTime(),
                event.getResourceBookings().stream()
                        .map(resourceBookingMapper::toDto)
                        .toList(),
                event.getRetailPrice(),
                event.getType(),
                event.getCreationTime(),
                event.canCancelWithoutPenalty(),
                event.calculateCancellationPenalty(),
                event.isCancelled(),
                event.getCancellationTime()
        );
    }

    public Event toEntity(EventCreationDto eventCreationDto, String organizerId) {
        Event event = new Event();
        event.setName(eventCreationDto.getName());
        event.setStartDateTime(eventCreationDto.getStartDateTime());
        event.setEndDateTime(eventCreationDto.getEndDateTime());
        event.setRetailPrice(eventCreationDto.getRetailPrice());
        event.setOrganizerId(organizerId);
        event.setType(eventCreationDto.getType());
        return event;
    }

    public Event updateFromDtoToEntity(EventUpdateDto eventUpdateDto, Event event, String organizerId){
        event.setName(eventUpdateDto.getName());
        event.setStartDateTime(eventUpdateDto.getStartDateTime());
        event.setEndDateTime(eventUpdateDto.getEndDateTime());
        event.setRetailPrice(eventUpdateDto.getRetailPrice());
        event.setOrganizerId(organizerId);
        List<ResourceBooking> resourceBookings = resourceBookingRepository.findAllById(eventUpdateDto.getBookingIds());
        event.getResourceBookings().clear();
        event.getResourceBookings().addAll(resourceBookings);
        return event;
    }
}
