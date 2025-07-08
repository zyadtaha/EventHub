package com.eventsystem.controller;

import com.eventsystem.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventsystem.service.ResourceBookingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource-bookings")
public class ResourceBookingController {
    private final ResourceBookingService resourceBookingService;

    public ResourceBookingController(ResourceBookingService resourceBookingService) {
        this.resourceBookingService = resourceBookingService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ResourceBookingDto> getAllBookings() {
        return resourceBookingService.getAllBookings();
    }

    @GetMapping("/organizer/my-bookings")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<ResourceBookingDto> getAllBookingsByOrganizer(Authentication connectedUser) {
        return resourceBookingService.getAllBookingsByOrganizer(connectedUser);
    }

    @GetMapping("/provider/my-bookings")
    @PreAuthorize("hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public List<ResourceBookingDto> getAllBookingsByProvider(Authentication connectedUser) {
        return resourceBookingService.getAllBookingsByProvider(connectedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public ResourceBookingDto getBookingById(@PathVariable Long id, Authentication connectedUser) {
        return resourceBookingService.getBookingById(id, connectedUser);
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResourceBookingDto createBooking(@RequestBody ResourceBookingCreationDto resourceBookingCreationDto, Authentication connectedUser) {
        return resourceBookingService.createBooking(resourceBookingCreationDto, connectedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public ResourceBookingDto updateBooking(@PathVariable Long id, @RequestBody ResourceBookingUpdateDto resourceBookingUpdateDto, Authentication connectedUser) {
        return resourceBookingService.updateBooking(id, resourceBookingUpdateDto, connectedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public void deleteBooking(@PathVariable Long id, Authentication connectedUser) {
        resourceBookingService.deleteBooking(id, connectedUser);
    }
}
