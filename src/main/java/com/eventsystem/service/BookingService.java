package com.eventsystem.service;

import com.eventsystem.dto.BookingCreationDto;
import com.eventsystem.dto.BookingDto;
import com.eventsystem.dto.BookingUpdateDto;
import com.eventsystem.mapper.BookingMapper;
import com.eventsystem.model.Booking;
import com.eventsystem.repository.BookingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository
                .findAll()
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookingsByOrganizer(Authentication connectedUser) {
        return bookingRepository
                .findByOrganizerId(connectedUser.getName())
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookingsByProvider(Authentication connectedUser) {
        return bookingRepository
                .findByProviderId(connectedUser.getName())
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id, Authentication connectedUser) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER") && booking.getOrganizerId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_VENUE_PROVIDER") && booking.getVenue() != null && booking.getVenue().getProviderId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OFFERING_PROVIDER") && booking.getOffering() != null && booking.getOffering().getProviderId().equals(connectedUser.getName()))
        ) {
            return bookingMapper.toDto(booking);
        } else {
            throw new IllegalArgumentException("You are not authorized to view this booking");
        }
    }

    public BookingDto createBooking(BookingCreationDto bookingCreationDto, String organizerId) {
        Booking booking = bookingMapper.toEntity(bookingCreationDto, organizerId);
        if(!booking.getEvent().getOrganizerId().equals(organizerId)){
            throw new IllegalArgumentException("You are not authorized to create a booking to this event");
        }
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    public BookingDto updateBooking(Long id, BookingUpdateDto newBooking, Authentication connectedUser) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER") && booking.getOrganizerId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_VENUE_PROVIDER") && booking.getVenue() != null && booking.getVenue().getProviderId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OFFERING_PROVIDER") && booking.getOffering() != null && booking.getOffering().getProviderId().equals(connectedUser.getName()))
        ) {
            Booking b = bookingMapper.updateFromDtoToEntity(newBooking, booking);
            bookingRepository.save(b);
            return bookingMapper.toDto(b);
        } else {
            throw new IllegalArgumentException("You are not authorized to update this booking");
        }
    }

    public void deleteBooking(Long id, Authentication connectedUser) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER") && booking.getOrganizerId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_VENUE_PROVIDER") && booking.getVenue() != null && booking.getVenue().getProviderId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OFFERING_PROVIDER") && booking.getOffering() != null && booking.getOffering().getProviderId().equals(connectedUser.getName()))
        ) {
            bookingRepository.delete(booking);
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this booking");
        }
    }
}

