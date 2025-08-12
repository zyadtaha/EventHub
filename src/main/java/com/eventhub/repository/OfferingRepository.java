package com.eventhub.repository;

import com.eventhub.model.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    Page<Offering> findByCreatedBy(String createdBy, Pageable pageable);
}
