package com.eventsystem.controller;

import com.eventsystem.dto.OfferingDto;
import com.eventsystem.model.Offering;
import com.eventsystem.service.OfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offerings")
@PreAuthorize("hasRole('OFFERING_PROVIDER')")
@Tag(name = "Offering", description = "Manage offerings for events")
public class OfferingController {
    private final OfferingService offeringService;

    public OfferingController(OfferingService offeringService) {
        this.offeringService = offeringService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Get all offerings", description = "Retrieve a list of all available offerings.")
    public List<OfferingDto> getAllOfferings() {
        return offeringService.getAllOfferings();
    }

    @GetMapping("/my-offerings")
    @Operation(summary = "Get the offerings owned by the current provider", description = "Retrieve a list of all offerings managed by the authenticated offering provider.")
    public List<OfferingDto> getAllOfferingsByProvider(Authentication connectedUser) {
        return offeringService.getAllOfferingsByProvider(connectedUser.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OFFERING_PROVIDER') or hasRole('ORGANIZER')")
    @Operation(summary = "Get an offering by ID", description = "Retrieve the details of a specific offering by its ID.")
    public OfferingDto getOfferingById(@PathVariable Long id, Authentication connectedUser) {
        return offeringService.getOfferingById(id, connectedUser);
    }

    @PostMapping
    @Operation(summary = "Create a new offering", description = "Create a new offering managed by the authenticated offering provider.")
    public OfferingDto createOffering(@RequestBody OfferingDto offeringDto, Authentication connectedUser) {
        return offeringService.createOffering(offeringDto, connectedUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing offering", description = "Update the details of an existing offering managed by the authenticated offering provider.")
    public OfferingDto updateOffering(@PathVariable Long id, @RequestBody OfferingDto offeringDto, Authentication connectedUser) {
        return offeringService.updateOffering(id, offeringDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an offering", description = "Delete an offering managed by the authenticated offering provider.")
    public void deleteOffering(@PathVariable Long id, Authentication connectedUser) {
        offeringService.deleteOffering(id, connectedUser.getName());
    }

    @GetMapping("/{id}/options")
    @PreAuthorize("hasRole('OFFERING_PROVIDER') or hasRole('ORGANIZER')")
    @Operation(summary = "Get the options of a specific offering", description = "Retrieve a list of all options available for a specific offering.")
    public List<Offering.Option> getAllOptions(@PathVariable Long id) {
        return offeringService.getAllOptions(id);
    }

    @PostMapping("/{id}/options")
    @Operation(summary = "Add an option to an offering", description = "Add a new option to a specific offering managed by the authenticated offering provider.")
    public OfferingDto addOption(
            @PathVariable Long id,
            @RequestBody Offering.Option option,
            Authentication connectedUser) {
        return offeringService.addOption(id, option, connectedUser.getName());
    }

    @GetMapping("/{id}/availability")
    @PreAuthorize("hasRole('OFFERING_PROVIDER') or hasRole('ORGANIZER')")
    @Operation(summary = "Get the availability slots of a specific offering", description = "Retrieve a list of all availability slots for a specific offering.")
    public List<Offering.AvailabilitySlot> getAllAvailabilitySlot(@PathVariable Long id) {
        return offeringService.getAllAvailabilitySlot(id);
    }

    @PostMapping("/{id}/availability")
    @Operation(summary = "Add an availability slot to an offering", description = "Add a new availability slot to a specific offering managed by the authenticated offering provider.")
    public OfferingDto addAvailabilitySlot(
            @PathVariable Long id,
            @RequestBody Offering.AvailabilitySlot availabilitySlot,
            Authentication connectedUser) {
        return offeringService.addAvailabilitySlot(id, availabilitySlot.getStartTime(), availabilitySlot.getEndTime(), connectedUser.getName());
    }
}