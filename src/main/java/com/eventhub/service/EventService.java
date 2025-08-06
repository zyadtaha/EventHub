package com.eventhub.service;

import com.eventhub.dto.event.EventCreationDto;
import com.eventhub.dto.event.EventDto;
import com.eventhub.dto.event.EventUpdateDto;
import com.eventhub.mapper.EventMapper;
import com.eventhub.model.Event;
import com.eventhub.repository.EventRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    EventRepository eventRepository;
    EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public List<EventDto> getAllEvents() {
        return eventRepository
                .findAll()
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventDto> getAllEventsByOrganizer(String organizerId) {
        return eventRepository
                .findByOrganizerId(organizerId)
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventById(Long id, Authentication connectedUser) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ATTENDEE")) ||
                event.getOrganizerId().equals(connectedUser.getName())) {
            return eventMapper.toDto(event);
        } else {
            throw new IllegalArgumentException("You are not authorized to view this event");
        }
    }

    public EventDto createEvent(EventCreationDto eventDto, String organizerId) {
        Event event = eventMapper.toEntity(eventDto, organizerId);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public EventDto updateEvent(Long id, EventUpdateDto eventDto, String organizerId) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to update this event");
        }
        event = eventMapper.updateFromDtoToEntity(eventDto, event, organizerId);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public void cancelEvent(Long id, String organizerId) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to cancel this event");
        }
        event.setCancelled(true);
        event.setCancellationTime(java.time.LocalDateTime.now());
        eventRepository.save(event);
    }
}
