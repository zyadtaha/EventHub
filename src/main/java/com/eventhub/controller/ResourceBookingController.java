package com.eventhub.controller;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventhub.dto.resourcebooking.ResourceBookingDto;
import com.eventhub.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventhub.service.ResourceBookingService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Resource Booking", description = "Manage resource bookings for events")
public class ResourceBookingController {
    private final ResourceBookingService resourceBookingService;

    public ResourceBookingController(ResourceBookingService resourceBookingService) {
        this.resourceBookingService = resourceBookingService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all resource bookings", description = "Retrieve a list of all resource bookings.")
    public PageResponse<ResourceBookingDto> getAllBookings(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "20", required = false) int pageSize
    ) {
        return resourceBookingService.getAllBookings(pageNumber, pageSize);
    }

    @GetMapping("/orgnaizer")
    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "Get bookings for the current organizer", description = "Retrieve bookings filtered by the authenticated organizer.")
    public PageResponse<ResourceBookingDto> getBookingsByOrganizerId(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ) {
        return resourceBookingService.getBookingsByOrganizerId(pageNumber, pageSize, connectedUser.getName());
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Get bookings for the current provider", description = "Retrieve bookings filtered by the authenticated provider.")
    public PageResponse<ResourceBookingDto> getBookingsByProviderId(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ) {
        return resourceBookingService.getBookingsByProviderId(pageNumber, pageSize, connectedUser.getName());
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

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Cancel a resource booking", description = "Cancel an existing resource booking.")
    public void cancelBooking(@PathVariable Long id, Authentication connectedUser) {
        resourceBookingService.cancelBooking(id, connectedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    @Operation(summary = "Update an existing resource booking", description = "Update the details of an existing resource booking.")
    public ResourceBookingDto updateBooking(@PathVariable Long id, @RequestBody ResourceBookingUpdateDto resourceBookingUpdateDto, Authentication connectedUser) {
        return resourceBookingService.updateBooking(id, resourceBookingUpdateDto, connectedUser);
    }
}
