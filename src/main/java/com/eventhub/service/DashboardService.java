package com.eventhub.service;

import com.eventhub.dto.DashboardDto;
import com.eventhub.model.Event;
import com.eventhub.model.EventRegistration;
import com.eventhub.model.Venue;
import com.eventhub.repository.EventRegistrationRepository;
import com.eventhub.repository.EventRepository;
import com.eventhub.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final VenueRepository venueRepository;

    public DashboardService(EventRepository eventRepository, EventRegistrationRepository registrationRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
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

        List<EventRegistration> registrations = registrationRepository.findAll();
        List<Integer> dailyBookingCount = new ArrayList<>(), dailyCancellationCount = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int days = 7;
        for (int i = days - 1; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            int bookings = (int) registrations.stream()
                    .filter(r -> r.getCreatedAt().toLocalDate().equals(day))
                    .count();
            int cancellations = (int) registrations.stream()
                    .filter(r -> r.getCancellationTime() != null && r.getCancellationTime().toLocalDate().equals(day))
                    .count();
            dailyBookingCount.add(bookings);
            dailyCancellationCount.add(cancellations);
        }
        dashboardDto.dailyBookingCount = dailyBookingCount;
        dashboardDto.dailyCancellationCount = dailyCancellationCount;

        List<Venue> venues = venueRepository.findAll();
        int bookedVenues = 0, totalVenues = 0;
        for (Venue venue : venues) {
            if (venue.getResourceBookings() != null) {
                bookedVenues++;
            }
            totalVenues++;
        }
        dashboardDto.venueUtilizationRate = (double) bookedVenues / totalVenues;

        dashboardDto.eventTypeDistribution = events.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getType().name(),
                        Collectors.reducing(0, e -> 1, Integer::sum)
                ));
        return dashboardDto;
    }
}