package com.eventsystem.repository;

import com.eventsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByOrganizerId(String organizerId);
    List<Booking> findByProviderId(String providerId);
}