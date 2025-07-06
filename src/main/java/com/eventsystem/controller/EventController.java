package com.eventsystem.controller;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.service.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@PreAuthorize("hasRole('ORGANIZER')")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ATTENDEE')")
    public List<EventDto> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/my-events")
    public List<EventDto> getAllEventsByOrganizer(Authentication connectedUser){
        return eventService.getAllEventsByOrganizer(connectedUser.getName());
    }

    // TODO: add an endpoint to get a venue by its ID

    @PostMapping()
    public EventDto createEvent(@RequestBody EventCreationDto eventDto, Authentication connectedUser){
        return eventService.createEvent(eventDto, connectedUser.getName());
    }

    @PutMapping("/{id}")
    public EventDto updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto eventDto, Authentication connectedUser){
        return eventService.updateEvent(id, eventDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id, Authentication connectedUser){
        eventService.deleteEvent(id, connectedUser.getName());
    }
}
