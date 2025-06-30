package com.eventsystem.controller;

import com.eventsystem.dto.OfferingDto;
import com.eventsystem.model.Offering;
import com.eventsystem.service.OfferingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/offerings")
public class OfferingController {
    private final OfferingService offeringService;

    public OfferingController(OfferingService offeringService) {
        this.offeringService = offeringService;
    }

    @GetMapping
    public List<OfferingDto> getAllOfferings() {
        return offeringService.getAllOfferings();
    }

    @PostMapping
    public OfferingDto createOffering(@RequestBody Offering offering) {
        return offeringService.createOffering(offering);
    }

    @PutMapping("/{id}")
    public OfferingDto updateOffering(@PathVariable Long id, @RequestBody Offering offering) {
        return offeringService.updateOffering(id, offering);
    }

    @DeleteMapping("/{id}")
    public void deleteOffering(@PathVariable Long id) {
        offeringService.deleteOffering(id);
    }

    @GetMapping("/{id}/options")
    public List<Offering.Option> getAllOptions(@PathVariable Long id) {
        return offeringService.getAllOptions(id);
    }

    @PostMapping("/{id}/options")
    public OfferingDto addOption(
            @PathVariable Long id,
            @RequestBody Offering.Option option) {
        return offeringService.addOption(id, option);
    }

    @GetMapping("/{id}/availability")
    public List<Offering.AvailabilitySlot> getAllAvailabilitySlot(@PathVariable Long id) {
        return offeringService.getAllAvailabilitySlot(id);
    }

    @PostMapping("/{id}/availability")
    public OfferingDto addAvailabilitySlot(
            @PathVariable Long id,
            @RequestBody Offering.AvailabilitySlot availabilitySlot) {
        return offeringService.addAvailabilitySlot(id, availabilitySlot.getStartTime(), availabilitySlot.getEndTime());
    }
}