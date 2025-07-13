package com.eventsystem.controller;

import com.eventsystem.dto.VenueDto;
import com.eventsystem.model.Venue;
import com.eventsystem.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
@PreAuthorize("hasRole('VENUE_PROVIDER')")
@Tag(name = "Venue", description = "Manage venues for events")
public class VenueController {
    private VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Get all venues", description = "Retrieve a list of all available venues.")
    public List<VenueDto> getAllVenues(){
        return venueService.getAllVenues();
    }

    @GetMapping("/my-venues")
    @Operation(summary = "Get the venues owned by the current provider", description = "Retrieve a list of all venues managed by the authenticated venue provider.")
    public List<VenueDto> getAllVenuesByProvider(Authentication connectedUser){
        return venueService.getAllVenuesByProvider(connectedUser.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('VENUE_PROVIDER') or hasRole('ORGANIZER')")
    @Operation(summary = "Get a venue by ID", description = "Retrieve the details of a specific venue by its ID.")
    public VenueDto getVenueById(@RequestParam Long id, Authentication connectedUser){
        return venueService.getVenueById(id, connectedUser);
    }

    @PostMapping()
    @Operation(summary = "Create a new venue", description = "Create a new venue managed by the authenticated venue provider.")
    public VenueDto createVenue(@RequestBody VenueDto venueDto, Authentication connectedUser){
        return venueService.createVenue(venueDto, connectedUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing venue", description = "Update the details of an existing venue managed by the authenticated venue provider.")
    public VenueDto updateVenue(@PathVariable Long id, @RequestBody VenueDto venueDto, Authentication connectedUser){
        return venueService.updateVenue(id, venueDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a venue", description = "Delete a venue managed by the authenticated venue provider.")
    public void deleteVenue(@PathVariable Long id, Authentication connectedUser){
        venueService.deleteVenue(id, connectedUser.getName());
    }
}
