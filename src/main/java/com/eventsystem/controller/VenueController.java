package com.eventsystem.controller;

import com.eventsystem.dto.VenueDto;
import com.eventsystem.model.Venue;
import com.eventsystem.service.VenueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
@PreAuthorize("hasRole('VENUE_PROVIDER')")
public class VenueController {
    private VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<VenueDto> getAllVenues(){
        return venueService.getAllVenues();
    }

    @GetMapping("/my-venues")
    public List<VenueDto> getAllVenuesByProvider(Authentication connectedUser){
        return venueService.getAllVenuesByProvider(connectedUser.getName());
    }

    // TODO: add an endpoint to get a venue by its ID

    @PostMapping()
    public VenueDto createVenue(@RequestBody VenueDto venueDto, Authentication connectedUser){
        return venueService.createVenue(venueDto, connectedUser.getName());
    }

    @PutMapping("/{id}")
    public VenueDto updateVenue(@PathVariable Long id, @RequestBody VenueDto venueDto, Authentication connectedUser){
        return venueService.updateVenue(id, venueDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteVenue(@PathVariable Long id, Authentication connectedUser){
        venueService.deleteVenue(id, connectedUser.getName());
    }
}
