package com.eventsystem.repository;

import com.eventsystem.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByProviderId(String providerId);
}
