package com.eventhub.repository;

import com.eventhub.model.Offering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    List<Offering> findByProviderId(String providerId);
}
