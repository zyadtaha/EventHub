package com.eventsystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "offerings")
public class Offering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer price;

    @Enumerated(EnumType.STRING)
    private OfferingType type;

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

    public Offering(String name, Integer price, OfferingType type, List<Option> options, List<String> offeringAreas, List<AvailabilitySlot> availabilitySlots) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.options = options;
        this.offeringAreas = offeringAreas;
        this.availabilitySlots = availabilitySlots;
    }

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