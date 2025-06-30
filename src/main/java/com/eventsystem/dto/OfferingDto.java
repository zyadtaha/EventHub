package com.eventsystem.dto;

import com.eventsystem.model.Offering;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OfferingDto {
    private String name;
    private Integer price;
    private Offering.OfferingType type;
    private List<Offering.Option> options;
    private List<String> offeringAreas;
    private List<Offering.AvailabilitySlot> availabilitySlots;
}
