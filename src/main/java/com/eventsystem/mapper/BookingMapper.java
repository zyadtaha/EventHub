package com.eventsystem.mapper;

import com.eventsystem.dto.BookingCreationDto;
import com.eventsystem.dto.BookingDto;
import com.eventsystem.dto.BookingUpdateDto;
import com.eventsystem.model.Booking;
import com.eventsystem.model.Event;
import com.eventsystem.model.Offering;
import com.eventsystem.model.Venue;
import com.eventsystem.repository.EventRepository;
import com.eventsystem.repository.OfferingRepository;
import com.eventsystem.repository.VenueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final OfferingRepository offeringRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final OfferingMapper offeringMapper;

    public BookingDto toDto(Booking booking){
        return new BookingDto(
                booking.getEvent().getId(),
                booking.getVenue() == null ? null : venueMapper.toDto(booking.getVenue()),
                booking.getOffering() == null ? null : offeringMapper.toDto(booking.getOffering()),
                booking.getBookingTime(),
                booking.getTotalPrice(),
                booking.getCancellationTime(),
                booking.getStatus(),
                booking.isCancelled()
        );
    }

    public Booking toEntity(BookingCreationDto bookingCreationDto, String organizerId) {
        Event event = eventRepository.findById(bookingCreationDto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + bookingCreationDto.getEventId()));
        Booking booking = new Booking();
        booking.setEvent(event);
        if(bookingCreationDto.getVenueId() == null) {
            booking.setVenue(null);
        } else {
            Venue venue = venueRepository.findById(bookingCreationDto.getVenueId())
                    .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + bookingCreationDto.getVenueId()));
            booking.setVenue(venue);
            booking.setProviderId(venue.getProviderId());
        }
        if(bookingCreationDto.getOfferingId() == null) {
            booking.setOffering(null);
        } else {
            Offering offering = offeringRepository.findById(bookingCreationDto.getOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("Offering not found with id: " + bookingCreationDto.getOfferingId()));
            booking.setOffering(offering);
            booking.setProviderId(offering.getProviderId());
        }
        booking.setBookingTime(bookingCreationDto.getBookingTime());
        booking.setTotalPrice(bookingCreationDto.getTotalPrice());
        booking.setStatus(Booking.Status.PENDING);
        booking.setCancelled(false);
        booking.setOrganizerId(organizerId);
        return booking;
    }

    public Booking updateFromDtoToEntity(BookingUpdateDto bookingUpdateDto, Booking booking){
        booking.setStatus(bookingUpdateDto.getStatus());
        booking.setCancellationTime(bookingUpdateDto.getCancellationTime());
        booking.setCancelled(bookingUpdateDto.isCancelled());
        return booking;
    }
}
