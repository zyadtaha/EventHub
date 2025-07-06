package com.eventsystem.controller;

import com.eventsystem.dto.OfferingDto;
import com.eventsystem.model.Offering;
import com.eventsystem.service.OfferingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offerings")
@PreAuthorize("hasRole('OFFERING_PROVIDER')")
public class OfferingController {
    private final OfferingService offeringService;

    public OfferingController(OfferingService offeringService) {
        this.offeringService = offeringService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<OfferingDto> getAllOfferings() {
        return offeringService.getAllOfferings();
    }

    @GetMapping("/my-offerings")
    public List<OfferingDto> getAllOfferingsByProvider(Authentication connectedUser) {
        return offeringService.getAllOfferingsByProvider(connectedUser.getName());
    }

    // TODO: add an endpoint to get a offering by its ID

    @PostMapping
    public OfferingDto createOffering(@RequestBody OfferingDto offeringDto, Authentication connectedUser) {
        return offeringService.createOffering(offeringDto, connectedUser.getName());
    }

    @PutMapping("/{id}")
    public OfferingDto updateOffering(@PathVariable Long id, @RequestBody OfferingDto offeringDto, Authentication connectedUser) {
        return offeringService.updateOffering(id, offeringDto, connectedUser.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteOffering(@PathVariable Long id, Authentication connectedUser) {
        offeringService.deleteOffering(id, connectedUser.getName());
    }

    @GetMapping("/{id}/options")
    @PreAuthorize("hasRole('OFFERING_PROVIDER') or hasRole('ORGANIZER')")
    public List<Offering.Option> getAllOptions(@PathVariable Long id) {
        return offeringService.getAllOptions(id);
    }

    @PostMapping("/{id}/options")
    public OfferingDto addOption(
            @PathVariable Long id,
            @RequestBody Offering.Option option,
            Authentication connectedUser) {
        return offeringService.addOption(id, option, connectedUser.getName());
    }

    @GetMapping("/{id}/availability")
    @PreAuthorize("hasRole('OFFERING_PROVIDER') or hasRole('ORGANIZER')")
    public List<Offering.AvailabilitySlot> getAllAvailabilitySlot(@PathVariable Long id) {
        return offeringService.getAllAvailabilitySlot(id);
    }

    @PostMapping("/{id}/availability")
    public OfferingDto addAvailabilitySlot(
            @PathVariable Long id,
            @RequestBody Offering.AvailabilitySlot availabilitySlot,
            Authentication connectedUser) {
        return offeringService.addAvailabilitySlot(id, availabilitySlot.getStartTime(), availabilitySlot.getEndTime(), connectedUser.getName());
    }
}