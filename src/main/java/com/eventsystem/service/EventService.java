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

    public EventDto createEvent(EventCreationDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public EventDto updateEvent(Long id, EventUpdateDto eventDto) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        event = eventMapper.updateFromDtoToEntity(eventDto, event);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }
}
