package com.eventsystem.service;

import com.eventsystem.dto.DashboardDto;
import com.eventsystem.model.Event;
import com.eventsystem.model.Venue;
import com.eventsystem.repository.EventRepository;
import com.eventsystem.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public DashboardService(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    public DashboardDto getDashboardData() {
        DashboardDto dashboardDto = new DashboardDto();

        List<Event> events = eventRepository.findAll();
        int upcoming = 0, ongoing = 0, completed = 0, cancelled = 0;
        LocalDateTime now = LocalDateTime.now();
        for (Event event : events) {
            if (event.isCancelled()) {
                cancelled++;
            } else if (event.getStartDateTime().isBefore(now) && event.getEndDateTime().isAfter(now)) {
                ongoing++;
            } else if (event.getStartDateTime().isAfter(now)) {
                upcoming++;
            } else {
                completed++;
            }
        }
        dashboardDto.eventStatusCount = new DashboardDto.EventStatusCount(upcoming, ongoing, completed, cancelled);

        List<Venue> venues = venueRepository.findAll();
        int bookedVenues = 0, totalVenues = 0;
        for (Venue venue : venues) {
            if (venue.getResourceBookings() != null) {
                bookedVenues++;
            }
            totalVenues++;
        }
        dashboardDto.venueUtilizationRate = (double) bookedVenues / totalVenues;

        return dashboardDto;
    }
}