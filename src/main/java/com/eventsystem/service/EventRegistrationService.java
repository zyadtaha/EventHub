package com.eventsystem.service;

import com.eventsystem.dto.eventregistration.RegistrationCreationDto;
import com.eventsystem.dto.eventregistration.RegistrationDto;
import com.eventsystem.dto.eventregistration.RegistrationUpdateDto;
import com.eventsystem.mapper.RegistrationMapper;
import com.eventsystem.model.Event;
import com.eventsystem.model.EventRegistration;
import com.eventsystem.repository.EventRegistrationRepository;
import com.eventsystem.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {
    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final RegistrationMapper registrationMapper;

    public EventRegistrationService(EventRegistrationRepository registrationRepository, EventRepository eventRepository, RegistrationMapper registrationMapper) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.registrationMapper = registrationMapper;
    }

    public List<RegistrationDto> getAllRegistrations() {
        return registrationRepository.findAll()
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationDto> getAllRegistrationsByEvent(Long eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if(!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to view registrations for this event");
        }
        return registrationRepository
                .findByEventId(eventId)
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationDto> getAllRegistrationsByAttendee(String attendeeId) {
        return registrationRepository
                .findByAttendeeId(attendeeId)
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    public RegistrationDto getRegistrationById(Long id, String userId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(userId) && !registration.getOrganizerId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to view this registration");
        }
        return registrationMapper.toDto(registration);
    }

    public RegistrationDto createEventRegistration(RegistrationCreationDto registrationCreationDto, String attendeeName, String attendeeId) {
        EventRegistration er = registrationMapper.toEntity(registrationCreationDto, attendeeName, attendeeId);
        registrationRepository.save(er);
        return registrationMapper.toDto(er);
    }

    public RegistrationDto updateEventRegistration(Long id, RegistrationUpdateDto registrationUpdateDto, String attendeeId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(attendeeId)) {
            throw new IllegalArgumentException("You are not authorized to update this registration");
        }
        registration = registrationMapper.updateEntityFromDto(registrationUpdateDto, registration);
        registrationRepository.save(registration);
        return registrationMapper.toDto(registration);
    }

    public void cancelEventRegistration(Long id, String attendeeId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(attendeeId)) {
            throw new IllegalArgumentException("You are not authorized to cancel this registration");
        }
        registration.setCancelled(true);
        registration.setCancellationTime(java.time.LocalDateTime.now());
        registrationRepository.save(registration);
    }
}
