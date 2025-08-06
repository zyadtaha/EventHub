package com.eventhub.repository;

import com.eventhub.model.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    Page<Offering> findByProviderId(String providerId, Pageable pageable);
}
