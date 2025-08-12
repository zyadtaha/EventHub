package com.eventhub.model;

import com.eventhub.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "venues")
public class Venue extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String location;
    private Integer minCapacity;
    private Integer maxCapacity;
    private List<String> imageUrls;
    @Column(nullable = false)
    private Integer pricePerHour;
    @Column(nullable = false)
    private String providerEmail;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ResourceBooking> resourceBookings;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VenueType type;

    public enum VenueType {
        PRIVATE, PUBLIC, INSTITUTION, OUTDOOR, THEATER_ARENA
    }
}
