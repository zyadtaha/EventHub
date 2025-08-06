package com.eventhub.repository;

import com.eventhub.model.ResourceBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceBookingRepository extends JpaRepository<ResourceBooking, Long> {
    Page<ResourceBooking> findByOrganizerId(String organizerId, Pageable pageable);
    Page<ResourceBooking> findByProviderId(String providerId, Pageable pageable);
}