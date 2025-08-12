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

    public Venue toEntity(VenueDto venueDto, String providerEmail) {
        Venue venue = new Venue();
        venue.setName(venueDto.getName());
        venue.setType(venueDto.getType());
        venue.setLocation(venueDto.getLocation());
        venue.setMinCapacity(venueDto.getMinCapacity());
        venue.setMaxCapacity(venueDto.getMaxCapacity());
        venue.setPricePerHour(venueDto.getPricePerHour());
        venue.setMaxCapacity(venueDto.getMaxCapacity());
        venue.setProviderEmail(providerEmail);
        return venue;
    }
}
