package com.eventsystem.controller;

import com.eventsystem.dto.BookingCreationDto;
import com.eventsystem.dto.BookingDto;
import com.eventsystem.dto.BookingUpdateDto;
import com.eventsystem.service.BookingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/organizer/my-bookings")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<BookingDto> getAllBookingsByOrganizer(Authentication connectedUser) {
        return bookingService.getAllBookingsByOrganizer(connectedUser);
    }

    @GetMapping("/provider/my-bookings")
    @PreAuthorize("hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public List<BookingDto> getAllBookingsByProvider(Authentication connectedUser) {
        return bookingService.getAllBookingsByProvider(connectedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public BookingDto getBookingById(@PathVariable Long id, Authentication connectedUser) {
        return bookingService.getBookingById(id, connectedUser);
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public BookingDto createBooking(@RequestBody BookingCreationDto bookingCreationDto, Authentication connectedUser) {
        return bookingService.createBooking(bookingCreationDto, connectedUser.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody BookingUpdateDto bookingUpdateDto, Authentication connectedUser) {
        return bookingService.updateBooking(id, bookingUpdateDto, connectedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('VENUE_PROVIDER') or hasRole('OFFERING_PROVIDER')")
    public void deleteBooking(@PathVariable Long id, Authentication connectedUser) {
        bookingService.deleteBooking(id, connectedUser);
    }
}
