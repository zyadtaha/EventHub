package com.eventhub.service;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.event.EventCreationDto;
import com.eventhub.dto.event.EventDto;
import com.eventhub.dto.event.EventUpdateDto;
import com.eventhub.exception.NotAuthorizedException;
import com.eventhub.exception.NotAuthorizedException.*;
import com.eventhub.exception.NotFoundException;
import com.eventhub.exception.NotFoundException.*;
import com.eventhub.mapper.EventMapper;
import com.eventhub.model.Event;
import com.eventhub.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.eventhub.constant.SortConstant.START_DATE_TIME;

@Service
public class EventService {
    EventRepository eventRepository;
    EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public PageResponse<EventDto> getAllEvents(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(START_DATE_TIME).descending());
        Page<Event> events = eventRepository.findAll(pageable);
        List<EventDto> eventDtos = events
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                eventDtos,
                events.getNumber(),
                events.getSize(),
                events.getTotalElements(),
                events.getTotalPages(),
                events.isFirst(),
                events.isLast()
        );
    }

    public PageResponse<EventDto> getAllEventsByOrganizer(int pageNumber, int pageSize, String organizerId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(START_DATE_TIME).descending());
        Page<Event> events = eventRepository.findByCreatedBy(organizerId, pageable);
        List<EventDto> eventDtos = events
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                eventDtos,
                events.getNumber(),
                events.getSize(),
                events.getTotalElements(),
                events.getTotalPages(),
                events.isFirst(),
                events.isLast()
        );
    }

    public EventDto getEventById(Long id, Authentication connectedUser) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.EVENT));
        if (connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ATTENDEE")) ||
                event.getCreatedBy().equals(connectedUser.getName())) {
            return eventMapper.toDto(event);
        } else {
            throw new NotAuthorizedException(Action.VIEW, ResourceType.EVENT, id);
        }
    }

    public EventDto createEvent(EventCreationDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public EventDto updateEvent(Long id, EventUpdateDto eventDto, String organizerId) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.EVENT));
        if (!event.getCreatedBy().equals(organizerId)) {
            throw new NotAuthorizedException(Action.UPDATE, ResourceType.EVENT, id);
        }
        event = eventMapper.updateFromDtoToEntity(eventDto, event);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public void cancelEvent(Long id, String organizerId) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.EVENT));
        if (!event.getCreatedBy().equals(organizerId)) {
            throw new NotAuthorizedException(Action.CANCEL, ResourceType.EVENT, id);
        }
        event.setCancelled(true);
        event.setCancellationTime(java.time.LocalDateTime.now());
        eventRepository.save(event);
    }
}
