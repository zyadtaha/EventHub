package com.eventhub.mapper;

import com.eventhub.dto.VenueDto;
import com.eventhub.model.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {
    public VenueDto toDto(Venue venue) {
        return new VenueDto(venue.getName(),
                venue.getType(),
                venue.getLocation(),
                venue.getMinCapacity(),
                venue.getMaxCapacity(),
                venue.getImageUrls(),
                venue.getPricePerHour()
        );
    }

    public Venue toEntity(VenueDto venueDto, String providerId, String providerEmail) {
        return new Venue(venueDto.getName(),
                venueDto.getType(),
                venueDto.getLocation(),
                venueDto.getMinCapacity(),
                venueDto.getMaxCapacity(),
                venueDto.getPricePerHour(),
                providerId,
                providerEmail
        );
    }
}
