package com.eventhub.repository;

import com.eventhub.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByOrganizerId(String organizerId, Pageable pageable);
}
