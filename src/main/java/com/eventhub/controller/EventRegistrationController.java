package com.eventhub.controller;

import com.eventhub.dto.eventregistration.RegistrationCreationDto;
import com.eventhub.dto.eventregistration.RegistrationDto;
import com.eventhub.dto.eventregistration.RegistrationUpdateDto;
import com.eventhub.service.EventRegistrationService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: think of each endpoint, should the admin see it?
@RequestMapping("/api/v1/events/registrations")
@RestController
@Tag(name = "Event Registrations", description = "Managing event registrations")
public class EventRegistrationController {
    private final EventRegistrationService registrationService;

    public EventRegistrationController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all event registrations", description = "Retrieve a list of all event registrations.")
    public List<RegistrationDto> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }

    @GetMapping(params = "eventId")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Get the registrations of a specific event", description = "Retrieve a list of all registrations of an event organized by the authenticated organizer.")
    public List<RegistrationDto> getAllRegistrationsByEvent(@RequestParam Long eventId, Authentication connectedUser) {
        return registrationService.getAllRegistrationsByEvent(eventId, connectedUser.getName());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ATTENDEE')")
    @Operation(summary = "Get the registrations made by the current attendee", description = "Retrieve a list of all registrations made by the authenticated attendee.")
    public List<RegistrationDto> getAllRegistrationsByAttendee(Authentication connectedUser) {
        return registrationService.getAllRegistrationsByAttendee(connectedUser.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ATTENDEE')")
    @Operation(summary = "Get registration by ID", description = "Retrieve a specific event registration by its ID.")
    public RegistrationDto getRegistrationById(@PathVariable Long id, Authentication connectedUser) {
        return registrationService.getRegistrationById(id, connectedUser.getName());
    }

    @PostMapping
    @PreAuthorize("hasRole('ATTENDEE')")
    @Operation(summary = "Create a new event registration", description = "Create a new registration for an event.")
    public RegistrationDto createRegistration(@RequestBody RegistrationCreationDto registrationCreationDto, Authentication connectedUser) throws StripeException {
        return registrationService.createRegistration(registrationCreationDto, connectedUser);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ATTENDEE')")
    @Operation(summary = "Cancel an event registration", description = "Cancel an existing event registration.")
    public RegistrationDto cancelRegistration(@PathVariable Long id, Authentication connectedUser) {
        return registrationService.cancelRegistration(id, connectedUser.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ATTENDEE')")
    @Operation(summary = "Update an event registration", description = "Update an existing event registration.")
    public RegistrationDto updateRegistration(
            @PathVariable Long id,
            @RequestBody RegistrationUpdateDto registrationUpdateDto,
            Authentication connectedUser) {
        return registrationService.updateRegistration(id, registrationUpdateDto, connectedUser.getName());
    }
}