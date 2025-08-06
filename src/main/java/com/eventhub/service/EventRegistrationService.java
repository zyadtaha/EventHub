package com.eventhub.service;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.eventregistration.RegistrationCreationDto;
import com.eventhub.dto.eventregistration.RegistrationDto;
import com.eventhub.dto.eventregistration.RegistrationUpdateDto;
import com.eventhub.mapper.RegistrationMapper;
import com.eventhub.model.Event;
import com.eventhub.model.EventRegistration;
import com.eventhub.repository.EventRegistrationRepository;
import com.eventhub.repository.EventRepository;
import com.stripe.exception.StripeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PageResponse<RegistrationDto> getAllRegistrations(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("registrationDate").descending());
        Page<EventRegistration> registrations = registrationRepository.findAll(pageable);
        List<RegistrationDto> registrationDtos = registrations
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                registrationDtos,
                registrations.getNumber(),
                registrations.getSize(),
                registrations.getTotalElements(),
                registrations.getTotalPages(),
                registrations.isFirst(),
                registrations.isLast()
        );
    }

    public PageResponse<RegistrationDto> getAllRegistrationsByEvent(int pageNumber, int pageSize, Long eventId, String organizerId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if(!event.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to view registrations for this event");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("registrationDate").descending());
        Page<EventRegistration> registrations = registrationRepository.findByEventId(eventId, pageable);
        List<RegistrationDto> registrationDtos = registrations
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                registrationDtos,
                registrations.getNumber(),
                registrations.getSize(),
                registrations.getTotalElements(),
                registrations.getTotalPages(),
                registrations.isFirst(),
                registrations.isLast()
        );
    }

    public PageResponse<RegistrationDto> getAllRegistrationsByAttendee(int pageNumber, int pageSize, String attendeeId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("registrationDate").descending());
        Page<EventRegistration> registrations = registrationRepository.findByAttendeeId(attendeeId, pageable);
        List<RegistrationDto> registrationDtos = registrations
                .stream()
                .map(registrationMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                registrationDtos,
                registrations.getNumber(),
                registrations.getSize(),
                registrations.getTotalElements(),
                registrations.getTotalPages(),
                registrations.isFirst(),
                registrations.isLast()
        );
    }

    public RegistrationDto getRegistrationById(Long id, String userId) {
        EventRegistration registration = registrationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        if (!registration.getAttendeeId().equals(userId) && !registration.getOrganizerId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to view this registration");
        }
        return registrationMapper.toDto(registration);
    }

    @Transactional
    public RegistrationDto createRegistration(RegistrationCreationDto registrationCreationDto, Authentication connectedUser) throws StripeException {
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

    public RegistrationDto updateRegistration(Long id, RegistrationUpdateDto registrationUpdateDto, String attendeeId) {
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

    public RegistrationDto cancelRegistration(Long id, String attendeeId) {
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
        return registrationMapper.toDto(registration);
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
