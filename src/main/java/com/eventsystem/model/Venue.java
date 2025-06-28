package com.eventsystem.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String location;
    private Integer minCapacity;
    private Integer maxCapacity;
    private List<String> imageUrls;
    private Integer pricePerHour;

    public Venue(String name, String type, String location, Integer minCapacity, Integer maxCapacity, Integer pricePerHour) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.pricePerHour = pricePerHour;
    }
}
