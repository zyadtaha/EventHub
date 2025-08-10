package com.eventhub.mapper;

import com.eventhub.dto.eventregistration.RegistrationCreationDto;
import com.eventhub.dto.eventregistration.RegistrationDto;
import com.eventhub.dto.eventregistration.RegistrationUpdateDto;
import com.eventhub.model.Event;
import com.eventhub.model.EventRegistration;
import com.eventhub.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegistrationMapper {
    private final EventRepository eventRepository;

    public RegistrationDto toDto(EventRegistration eventRegistration) {
        Event e = eventRepository.findById(eventRegistration.getEventId()).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return new RegistrationDto(
                e.getName(),
                eventRegistration.getAttendeeName(),
                e.getRetailPrice(),
                eventRegistration.getStatus()
        );
    }

    public EventRegistration toEntity(RegistrationCreationDto registrationCreationDto, String attendeeName, String attendeeId, String attendeeEmail) {
        Event event = eventRepository.findById(registrationCreationDto.getEventId()).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        EventRegistration registration = new EventRegistration();
        registration.setEventId(event.getId());
        registration.setAttendeeId(attendeeId);
        registration.setAttendeeName(attendeeName);
        registration.setAttendeeEmail(attendeeEmail);
        registration.setOrganizerId(event.getCreatedBy());
        registration.setStatus(EventRegistration.RegistrationStatus.WAITLISTED);
        return registration;
    }

    public EventRegistration updateEntityFromDto(RegistrationUpdateDto registrationUpdateDto, EventRegistration registration) {
        registration.setCancellationTime(registrationUpdateDto.getCancellationTime());
        registration.setStatus(registrationUpdateDto.getStatus());
        registration.setCancelled(registrationUpdateDto.isCancelled());
        return registration;
    }
}
