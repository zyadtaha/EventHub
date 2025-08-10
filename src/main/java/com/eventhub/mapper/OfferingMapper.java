package com.eventhub.mapper;

import com.eventhub.dto.OfferingDto;
import com.eventhub.model.Offering;
import org.springframework.stereotype.Component;

@Component
public class OfferingMapper {
    public OfferingDto toDto(Offering offering) {
        return new OfferingDto(
                offering.getName(),
                offering.getPrice(),
                offering.getType(),
                offering.getOptions(),
                offering.getOfferingAreas(),
                offering.getAvailabilitySlots()
        );
    }

    public Offering toEntity(OfferingDto offeringDto, String providerId, String providerEmail) {
        return new Offering(
                offeringDto.getName(),
                offeringDto.getPrice(),
                offeringDto.getType(),
                offeringDto.getOptions(),
                offeringDto.getOfferingAreas(),
                offeringDto.getAvailabilitySlots(),
                providerId,
                providerEmail
        );
    }
}
