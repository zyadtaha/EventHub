package com.eventsystem.mapper;

import com.eventsystem.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventsystem.model.ResourceBooking;
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
public class ResourceBookingMapper {
    private final OfferingRepository offeringRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final OfferingMapper offeringMapper;

    public ResourceBookingDto toDto(ResourceBooking resourceBooking){
        return new ResourceBookingDto(
                resourceBooking.getEvent().getId(),
                resourceBooking.getVenue() == null ? null : venueMapper.toDto(resourceBooking.getVenue()),
                resourceBooking.getOffering() == null ? null : offeringMapper.toDto(resourceBooking.getOffering()),
                resourceBooking.getBookingTime(),
                resourceBooking.getTotalPrice(),
                resourceBooking.getCancellationTime(),
                resourceBooking.getStatus(),
                resourceBooking.isCancelled()
        );
    }

    public ResourceBooking toEntity(ResourceBookingCreationDto resourceBookingCreationDto, String organizerId) {
        Event event = eventRepository.findById(resourceBookingCreationDto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + resourceBookingCreationDto.getEventId()));
        ResourceBooking resourceBooking = new ResourceBooking();
        resourceBooking.setEvent(event);
        if(resourceBookingCreationDto.getVenueId() == null) {
            resourceBooking.setVenue(null);
        } else {
            Venue venue = venueRepository.findById(resourceBookingCreationDto.getVenueId())
                    .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + resourceBookingCreationDto.getVenueId()));
            resourceBooking.setVenue(venue);
            resourceBooking.setProviderId(venue.getProviderId());
        }
        if(resourceBookingCreationDto.getOfferingId() == null) {
            resourceBooking.setOffering(null);
        } else {
            Offering offering = offeringRepository.findById(resourceBookingCreationDto.getOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("Offering not found with id: " + resourceBookingCreationDto.getOfferingId()));
            resourceBooking.setOffering(offering);
            resourceBooking.setProviderId(offering.getProviderId());
        }
        resourceBooking.setBookingTime(resourceBookingCreationDto.getBookingTime());
        resourceBooking.setTotalPrice(resourceBookingCreationDto.getTotalPrice());
        resourceBooking.setStatus(ResourceBooking.Status.PENDING);
        resourceBooking.setCancelled(false);
        resourceBooking.setOrganizerId(organizerId);
        return resourceBooking;
    }

    public ResourceBooking updateEntityFromDto(ResourceBookingUpdateDto resourceBookingUpdateDto, ResourceBooking resourceBooking){
        resourceBooking.setStatus(resourceBookingUpdateDto.getStatus());
        resourceBooking.setCancellationTime(resourceBookingUpdateDto.getCancellationTime());
        resourceBooking.setCancelled(resourceBookingUpdateDto.isCancelled());
        return resourceBooking;
    }
}
