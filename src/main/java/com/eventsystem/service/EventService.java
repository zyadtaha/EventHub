package com.eventsystem.service;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.mapper.EventMapper;
import com.eventsystem.model.Event;
import com.eventsystem.repository.EventRepository;
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

    public void deleteEvent(Long id, String organizerId) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to delete this event");
        }
        eventRepository.delete(event);
    }
}
