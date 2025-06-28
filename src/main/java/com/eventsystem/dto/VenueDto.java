package com.eventsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VenueDto {
    private String name;
    private String type;
    private String location;
    private Integer minCapacity;
    private Integer maxCapacity;
    private List<String> imageUrls;
    private Integer pricePerHour;
}
