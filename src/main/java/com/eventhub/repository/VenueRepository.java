package com.eventhub.repository;

import com.eventhub.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByProviderId(String providerId);
}
