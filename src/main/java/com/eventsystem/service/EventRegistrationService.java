package com.eventsystem.service;

import com.eventsystem.dto.eventregistration.RegistrationCreationDto;
import com.eventsystem.dto.eventregistration.RegistrationDto;
import com.eventsystem.dto.eventregistration.RegistrationUpdateDto;
import com.eventsystem.mapper.RegistrationMapper;
import com.eventsystem.model.Event;
import com.eventsystem.model.EventRegistration;
import com.eventsystem.repository.EventRegistrationRepository;
import com.eventsystem.repository.EventRepository;
import com.eventsystem.utils.EmailService;
import com.stripe.exception.StripeException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {
    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final RegistrationMapper registrationMapper;
    private final EmailService emailService;
    private final StripeService stripeService;

    public EventRegistrationService(EventRegistrationRepository registrationRepository, EventRepository eventRepository, RegistrationMapper registrationMapper, EmailService emailService, StripeService stripeService) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.registrationMapper = registrationMapper;
        this.emailService = emailService;
        this.stripeService = stripeService;
    }

    public List<RegistrationDto> getAllRegistrations() {
        return registrationRepository.findAll()
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationDto> getAllRegistrationsByEvent(Long eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if(!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to view registrations for this event");
        }
        return registrationRepository
                .findByEventId(eventId)
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationDto> getAllRegistrationsByAttendee(String attendeeId) {
        return registrationRepository
                .findByAttendeeId(attendeeId)
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());
    }

    public RegistrationDto getRegistrationById(Long id, String userId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(userId) && !registration.getOrganizerId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to view this registration");
        }
        return registrationMapper.toDto(registration);
    }

    @Transactional
    public RegistrationDto createEventRegistration(RegistrationCreationDto registrationCreationDto, Authentication connectedUser) throws StripeException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String attendeeName = jwt.getClaimAsString("preferred_username");
        String attendeeEmail = jwt.getClaimAsString("email");
        String attendeeId = connectedUser.getName();

        EventRegistration registration = registrationMapper.toEntity(registrationCreationDto, attendeeName, attendeeId, attendeeEmail);
        registrationRepository.save(registration);

        Event event = eventRepository.findById(registrationCreationDto.getEventId()).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        String paymentUrl = stripeService.createPaymentLink(
                BigDecimal.valueOf(event.getRetailPrice()),
                "USD",
                "Registration for: " + event.getName(),
                registration.getId(),
                true
        );

        emailService.sendRegistrationPaymentRequest(attendeeEmail, event, paymentUrl);
        return registrationMapper.toDto(registration);
    }

    public RegistrationDto updateEventRegistration(Long id, RegistrationUpdateDto registrationUpdateDto, String attendeeId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(attendeeId)) {
            throw new IllegalArgumentException("You are not authorized to update this registration");
        }
        registration = registrationMapper.updateEntityFromDto(registrationUpdateDto, registration);
        registrationRepository.save(registration);

        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        emailService.sendAttendeeUpdate(registration.getAttendeeEmail(), event);

        return registrationMapper.toDto(registration);
    }

    public void cancelEventRegistration(Long id, String attendeeId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(attendeeId)) {
            throw new IllegalArgumentException("You are not authorized to cancel this registration");
        }
        registration.setCancelled(true);
        registration.setCancellationTime(java.time.LocalDateTime.now());
        registration.setStatus(EventRegistration.RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);

        Event event = eventRepository.findById(registration.getEventId()).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        emailService.sendAttendeeCancellation(registration.getAttendeeEmail(), event);
    }

    @Transactional
    public void confirmPayment(Long registrationId, String attendeeId) {
        EventRegistration registration = registrationRepository.findById(registrationId).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(attendeeId)) {
            throw new IllegalArgumentException("You are not authorized to pay for this registration");
        }
        registration.setStatus(EventRegistration.RegistrationStatus.CONFIRMED);
        registrationRepository.save(registration);

        Event event = eventRepository.findById(registration.getEventId()).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        emailService.sendAttendeeInvitation(registration.getAttendeeEmail(), event);
    }
}
