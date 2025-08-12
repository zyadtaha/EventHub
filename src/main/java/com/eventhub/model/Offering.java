package com.eventhub.model;

import com.eventhub.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "offerings")
public class Offering extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private String providerEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferingType type;

    // TODO: Should it be initialized with empty list?
    @OneToMany(mappedBy = "offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ResourceBooking> resourceBookings;

    @ElementCollection
    @CollectionTable(name = "offering_options", joinColumns = @JoinColumn(name = "offering_id"))
    private List<Option> options;

    @ElementCollection
    @CollectionTable(name = "offering_areas", joinColumns = @JoinColumn(name = "offering_id"))
    @Column(name = "location")
    private List<String> offeringAreas;

    @ElementCollection
    @CollectionTable(name = "offering_availability_slots", joinColumns = @JoinColumn(name = "offering_id"))
    private List<AvailabilitySlot> availabilitySlots;

    public enum OfferingType {
        CATERING, DECORATION, AUDIO_VISUAL_EQUIPMENT,
        PHOTOGRAPHY, FURNITURE_RENTAL, SECURITY
    }

    @Data
    @Embeddable
    public static class Option {
        private String name;
        private Integer price;
    }

    @Data
    @Embeddable
    @NoArgsConstructor
    public static class AvailabilitySlot {
        @JsonProperty("startTime")
        private LocalDateTime startTime;
        @JsonProperty("endTime")
        private LocalDateTime endTime;

        public AvailabilitySlot(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    //TODO: check if the offering available at the event's time?
    public boolean isAvailableAt(LocalDateTime dateTime) {
        return availabilitySlots.stream()
                .anyMatch(slot ->
                        !dateTime.isBefore(slot.getStartTime()) &&
                                !dateTime.isAfter(slot.getEndTime()));
    }

    public void addOfferingArea(String location) {
        if (offeringAreas == null) {
            offeringAreas = new ArrayList<>();
        }
        offeringAreas.add(location.trim().toLowerCase());
    }

    public void addOption(Option option) {
        if (options == null) {
            options = new ArrayList<>();
        }
        options.add(option);
    }

    public void addAvailabilitySlot(AvailabilitySlot availabilitySlot){
        if (availabilitySlots == null) {
            availabilitySlots = new ArrayList<>();
        }
        availabilitySlots.add(availabilitySlot);
    }
}