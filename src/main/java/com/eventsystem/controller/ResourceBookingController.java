package com.eventsystem.controller;

import com.eventsystem.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventsystem.service.ResourceBookingService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource-bookings")
@Tag(name = "Resource Booking", description = "Manage resource bookings for events")
public class ResourceBookingController {
    private final ResourceBookingService resourceBookingService;

    public ResourceBookingController(ResourceBookingService resourceBookingService) {
        this.resourceBookingService = resourceBookingService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all resource bookings", description = "Retrieve a list of all resource bookings.")
    public List<ResourceBookingDto> getAllBookings() {
        return resourceBookingService.getAllBookings();
    }

    @GetMapping("/organizer/my-bookings")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Get the bookings made by the current organizer", description = "Retrieve a list of all bookings made by the authenticated organizer.")
    public List<ResourceBookingDto> getAllBookingsByOrganizer(Authentication connectedUser) {
        return resourceBookingService.getAllBookingsByOrganizer(connectedUser);
    }

    @GetMapping("/provider/my-bookings")
    @PreAuthorize("hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Get the bookings accepted by the current provider", description = "Retrieve a list of all bookings accepted by the authenticated venue or offering provider.")
    public List<ResourceBookingDto> getAllBookingsByProvider(Authentication connectedUser) {
        return resourceBookingService.getAllBookingsByProvider(connectedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Get booking by ID", description = "Retrieve a specific resource booking by its ID.")
    public ResourceBookingDto getBookingById(@PathVariable Long id, Authentication connectedUser) {
        return resourceBookingService.getBookingById(id, connectedUser);
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Create a new resource booking", description = "Create a new resource booking for an event.")
    public ResourceBookingDto createBooking(@RequestBody ResourceBookingCreationDto resourceBookingCreationDto, Authentication connectedUser) throws StripeException {
        return resourceBookingService.createBooking(resourceBookingCreationDto, connectedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Update an existing resource booking", description = "Update the details of an existing resource booking.")
    public ResourceBookingDto updateBooking(@PathVariable Long id, @RequestBody ResourceBookingUpdateDto resourceBookingUpdateDto, Authentication connectedUser) {
        return resourceBookingService.updateBooking(id, resourceBookingUpdateDto, connectedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Cancel a resource booking", description = "Cancel an existing resource booking.")
    public void cancelBooking(@PathVariable Long id, Authentication connectedUser) {
        resourceBookingService.cancelBooking(id, connectedUser);
    }
}
