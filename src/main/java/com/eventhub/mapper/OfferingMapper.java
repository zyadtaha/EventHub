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

    public Offering toEntity(OfferingDto offeringDto, String providerEmail) {
        Offering offering = new Offering();
        offering.setName(offeringDto.getName());
        offering.setPrice(offeringDto.getPrice());
        offering.setType(offeringDto.getType());
        offering.setOptions(offeringDto.getOptions());
        offering.setOfferingAreas(offeringDto.getOfferingAreas());
        offering.setAvailabilitySlots(offeringDto.getAvailabilitySlots());
        offering.setProviderEmail(providerEmail);
        return offering;
    }
}
