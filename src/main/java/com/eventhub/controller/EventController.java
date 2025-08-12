package com.eventhub.controller;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.event.EventCreationDto;
import com.eventhub.dto.event.EventDto;
import com.eventhub.dto.event.EventUpdateDto;
import com.eventhub.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public PageResponse<EventDto> getAllEvents(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "20", required = false) int pageSize
    ) {
        return eventService.getAllEvents(pageNumber, pageSize);
    }

    @GetMapping("/organizer")
    @Operation(summary = "Get the events created by the current organizer", description = "Retrieve a list of the events created by the authenticated organizer")
    public PageResponse<EventDto> getAllEventsByOrganizer(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ){
        return eventService.getAllEventsByOrganizer(pageNumber, pageSize, connectedUser.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ATTENDEE')")
    @Operation(summary = "Get an event by ID", description = "Retrieve the details of a specific event by its ID")
    public EventDto getEventById(@PathVariable Long id, Authentication connectedUser){
        return eventService.getEventById(id, connectedUser);
    }

    @PostMapping()
    @Operation(summary = "Create a new event", description = "Create a new event managed by the authenticated organizer")
    public EventDto createEvent(@RequestBody EventCreationDto eventDto){
        return eventService.createEvent(eventDto);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an event", description = "Cancel an event managed by the authenticated organizer")
    public void cancelEvent(@PathVariable Long id, Authentication connectedUser){
        eventService.cancelEvent(id, connectedUser.getName());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing event", description = "Update the details of an existing event managed by the authenticated organizer")
    public EventDto updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto eventDto, Authentication connectedUser){
        return eventService.updateEvent(id, eventDto, connectedUser.getName());
    }
}
