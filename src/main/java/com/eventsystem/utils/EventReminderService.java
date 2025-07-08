package com.eventsystem.utils;

import com.eventsystem.repository.EventRegistrationRepository;
import com.eventsystem.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EventReminderService {
    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EmailService emailService;

    public EventReminderService(EventRepository eventRepository, EventRegistrationRepository eventRegistrationRepository, EmailService emailService) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startOfDay = tomorrow.atStartOfDay();
        LocalDateTime endOfDay = tomorrow.atTime(23, 59, 59);

        eventRepository
                .findAll()
                .stream()
                .filter(event ->
                        !event.getDateTime().isBefore(startOfDay) &&
                                !event.getDateTime().isAfter(endOfDay)
                )
                .forEach(event -> {
                    eventRegistrationRepository.findAll().stream()
                            .filter(registration ->
                                    registration.getEventId().equals(event.getId())
                            )
                            .forEach(registration -> {
                                String email = registration.getAttendeeEmail();
                                emailService.sendAttendeeReminder(email, event);
                            });
                });
    }
}