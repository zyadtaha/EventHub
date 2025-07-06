package com.eventsystem.model;

import jakarta.persistence.*;
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

    @Column(nullable = false, updatable = false)
    private String providerId;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <Booking> bookings;

    // TODO: make the venue type compatible with the event
    public Venue(String name, String type, String location, Integer minCapacity, Integer maxCapacity, Integer pricePerHour, String providerId) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.pricePerHour = pricePerHour;
        this.providerId = providerId;
    }
}
