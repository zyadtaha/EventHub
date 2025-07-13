package com.eventsystem.controller;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@PreAuthorize("hasRole('ORGANIZER')")
@Tag(name = "Event", description = "Managing events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ATTENDEE')")
    @Operation(summary = "Get all events", description = "Retrieve a list of all available events")
    public List<EventDto> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/mine")
    @Operation(summary = "Get the events created by the current organizer", description = "Retrieve a list of the events created by the authenticated organizer")
    public List<EventDto> getAllEventsByOrganizer(Authentication connectedUser){
        return eventService.getAllEventsByOrganizer(connectedUser.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ATTENDEE')")
    @Operation(summary = "Get an event by ID", description = "Retrieve the details of a specific event by its ID")
    public EventDto getEventById(@PathVariable Long id, Authentication connectedUser){
        return eventService.getEventById(id, connectedUser);
    }

    @PostMapping()
    @Operation(summary = "Create a new event", description = "Create a new event managed by the authenticated organizer")
    public EventDto createEvent(@RequestBody EventCreationDto eventDto, Authentication connectedUser){
        return eventService.createEvent(eventDto, connectedUser.getName());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event", description = "Update the details of an existing event managed by the authenticated organizer")
    public EventDto updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto eventDto, Authentication connectedUser){
        return eventService.updateEvent(id, eventDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event", description = "Delete an existing event managed by the authenticated organizer")
    public void deleteEvent(@PathVariable Long id, Authentication connectedUser){
        eventService.deleteEvent(id, connectedUser.getName());
    }
}
