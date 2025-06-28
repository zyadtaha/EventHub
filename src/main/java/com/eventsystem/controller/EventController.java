package com.eventsystem.controller;

import com.eventsystem.dto.EventCreationDto;
import com.eventsystem.dto.EventDto;
import com.eventsystem.dto.EventUpdateDto;
import com.eventsystem.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public List<EventDto> getAllEvents(){
        return eventService.getAllEvents();
    }

    @PostMapping()
    public EventDto createEvent(@RequestBody EventCreationDto eventDto){
        return eventService.createEvent(eventDto);
    }

    @PutMapping("/{id}")
    public EventDto updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto eventDto){
        return eventService.updateEvent(id, eventDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
    }
}
