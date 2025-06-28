package com.eventsystem.mapper;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.model.Event;
import com.eventsystem.repository.VenueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventMapper {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public EventDto toDto(Event event) {
        return new EventDto(
                event.getName(),
                event.getDateTime(),
                venueMapper.toDto(event.getVenue()),
                event.getRetailPrice(),
                event.getCreationTime(),
                event.canCancelWithoutPenalty(),
                event.calculateCancellationPenalty()
        );
    }

    public Event toEntity(EventCreationDto eventCreationDto){
        Event event = new Event();
        event.setName(eventCreationDto.getName());
        event.setDateTime(eventCreationDto.getDateTime());
        event.setVenue(venueRepository.findById(eventCreationDto.getVenueId()).orElseThrow(()-> new IllegalStateException("Venue not found")));
        event.setRetailPrice(eventCreationDto.getRetailPrice());
        return event;
    }

    public Event updateFromDtoToEntity(EventUpdateDto eventUpdateDto, Event event){
        event.setName(eventUpdateDto.getName());
        event.setDateTime(eventUpdateDto.getDateTime());
        event.setVenue(venueRepository.findById(eventUpdateDto.getVenueId()).orElseThrow(()-> new IllegalStateException("Venue not found")));
        event.setRetailPrice(eventUpdateDto.getRetailPrice());
        return event;
    }
}
