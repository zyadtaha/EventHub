package com.eventhub.mapper;

import com.eventhub.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventhub.dto.resourcebooking.ResourceBookingDto;
import com.eventhub.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventhub.model.ResourceBooking;
import com.eventhub.model.Event;
import com.eventhub.model.Offering;
import com.eventhub.model.Venue;
import com.eventhub.repository.EventRepository;
import com.eventhub.repository.OfferingRepository;
import com.eventhub.repository.VenueRepository;
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
                resourceBooking.getCreatedAt(),
                resourceBooking.getTotalPrice(),
                resourceBooking.getCancellationTime(),
                resourceBooking.getStatus(),
                resourceBooking.isCancelled()
        );
    }

    public ResourceBooking toEntity(ResourceBookingCreationDto resourceBookingCreationDto) {
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
            resourceBooking.setProviderId(venue.getCreatedBy());
        }
        if(resourceBookingCreationDto.getOfferingId() == null) {
            resourceBooking.setOffering(null);
        } else {
            Offering offering = offeringRepository.findById(resourceBookingCreationDto.getOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("Offering not found with id: " + resourceBookingCreationDto.getOfferingId()));
            resourceBooking.setOffering(offering);
            resourceBooking.setProviderId(offering.getCreatedBy());
        }
        resourceBooking.setTotalPrice(resourceBookingCreationDto.getTotalPrice());
        resourceBooking.setStatus(ResourceBooking.Status.PENDING);
        resourceBooking.setCancelled(false);
        return resourceBooking;
    }

    public ResourceBooking updateEntityFromDto(ResourceBookingUpdateDto resourceBookingUpdateDto, ResourceBooking resourceBooking){
        resourceBooking.setStatus(resourceBookingUpdateDto.getStatus());
        resourceBooking.setCancellationTime(resourceBookingUpdateDto.getCancellationTime());
        resourceBooking.setCancelled(resourceBookingUpdateDto.isCancelled());
        return resourceBooking;
    }
}
