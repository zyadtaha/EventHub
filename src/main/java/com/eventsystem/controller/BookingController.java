package com.eventsystem.controller;

import com.eventsystem.dto.BookingCreationDto;
import com.eventsystem.dto.BookingDto;
import com.eventsystem.dto.BookingUpdateDto;
import com.eventsystem.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingCreationDto bookingCreationDto) {
        return bookingService.createBooking(bookingCreationDto);
    }

    @PutMapping("/{id}")
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody BookingUpdateDto bookingUpdateDto) {
        return bookingService.updateBooking(id, bookingUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
