package com.eventsystem.controller;

import com.eventsystem.dto.eventregistration.RegistrationCreationDto;
import com.eventsystem.dto.eventregistration.RegistrationDto;
import com.eventsystem.dto.eventregistration.RegistrationUpdateDto;
import com.eventsystem.service.EventRegistrationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: think of each endpoint, should the admin see it?
@RequestMapping("/events-registrations")
@RestController
public class EventRegistrationController {
    private final EventRegistrationService registrationService;

    public EventRegistrationController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistrationDto> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }

    @GetMapping("/organizer/my-registrations")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<RegistrationDto> getAllRegistrationsByEvent(@RequestParam Long eventId, Authentication connectedUser) {
        return registrationService.getAllRegistrationsByEvent(eventId, connectedUser.getName());
    }

    @GetMapping("/attendee/my-registrations")
    @PreAuthorize("hasRole('ATTENDEE')")
    public List<RegistrationDto> getAllRegistrationsByAttendee(Authentication connectedUser) {
        return registrationService.getAllRegistrationsByAttendee(connectedUser.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ATTENDEE')")
    public RegistrationDto getRegistrationById(@PathVariable Long id, Authentication connectedUser) {
        return registrationService.getRegistrationById(id, connectedUser.getName());
    }

    @PostMapping
    @PreAuthorize("hasRole('ATTENDEE')")
    public RegistrationDto createEventRegistration(@RequestBody RegistrationCreationDto registrationCreationDto, Authentication connectedUser) {
        return registrationService.createEventRegistration(registrationCreationDto, connectedUser);
    }

    @PutMapping
    @PreAuthorize("hasRole('ATTENDEE')")
    public RegistrationDto updateEventRegistration(
            @RequestParam Long id,
            @RequestBody RegistrationUpdateDto registrationUpdateDto,
            Authentication connectedUser) {
        return registrationService.updateEventRegistration(id, registrationUpdateDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ATTENDEE')")
    public void cancelEventRegistration(@PathVariable Long id, Authentication connectedUser) {
        registrationService.cancelEventRegistration(id, connectedUser.getName());
    }
}