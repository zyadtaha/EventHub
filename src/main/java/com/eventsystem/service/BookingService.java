package com.eventsystem.service;

import com.eventsystem.dto.BookingCreationDto;
import com.eventsystem.dto.BookingDto;
import com.eventsystem.dto.BookingUpdateDto;
import com.eventsystem.mapper.BookingMapper;
import com.eventsystem.model.Booking;
import com.eventsystem.repository.BookingRepository;
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
                .collect(Collectors.toUnmodifiableList());
    }

    public BookingDto getBookingById(Long id) {
        Booking booking =  bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        return bookingMapper.toDto(booking);
    }

    public BookingDto createBooking(BookingCreationDto bookingCreationDto) {
        Booking booking = bookingMapper.toEntity(bookingCreationDto);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    public BookingDto updateBooking(Long id, BookingUpdateDto newBooking) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        booking = bookingMapper.updateFromDtoToEntity(newBooking, booking);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}

