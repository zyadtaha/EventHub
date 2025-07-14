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
    private String location;
    private Integer minCapacity;
    private Integer maxCapacity;
    private List<String> imageUrls;
    private Integer pricePerHour;

    @Column(nullable = false, updatable = false)
    private String providerId;

    private String providerEmail;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ResourceBooking> resourceBookings;

    @Enumerated(EnumType.STRING)
    private VenueType type;

    public enum VenueType {
        PRIVATE, PUBLIC, INSTITUTION, OUTDOOR, THEATER_ARENA
    }

    public Venue(String name,
                 VenueType type,
                 String location,
                 Integer minCapacity,
                 Integer maxCapacity,
                 Integer pricePerHour,
                 String providerId,
                 String providerEmail) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.pricePerHour = pricePerHour;
        this.providerId = providerId;
        this.providerEmail = providerEmail;
    }
}
