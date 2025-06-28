package com.eventsystem.controller;

import com.eventsystem.dto.VenueDto;
import com.eventsystem.model.Venue;
import com.eventsystem.service.VenueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {
    private VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping()
    public List<VenueDto> getAllVenues(){
        return venueService.getAllVenues();
    }

    @PostMapping()
    public VenueDto createVenue(@RequestBody Venue venue){
        return venueService.createVenue(venue);
    }

    @PutMapping("/{id}")
    public VenueDto updateVenue(@PathVariable Long id, @RequestBody Venue venue){
        return venueService.updateVenue(id, venue);
    }

    @DeleteMapping("/{id}")
    public void deleteVenue(@PathVariable Long id){
        venueService.deleteVenue(id);
    }
}
