package com.eventsystem.service;

import com.eventsystem.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventsystem.mapper.ResourceBookingMapper;
import com.eventsystem.model.ResourceBooking;
import com.eventsystem.repository.ResourceBookingRepository;
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
public class ResourceBookingService {
    private final ResourceBookingRepository resourceBookingRepository;
    private final ResourceBookingMapper resourceBookingMapper;
    private final EmailService emailService;
    private final StripeService stripeService;

    public ResourceBookingService(ResourceBookingRepository resourceBookingRepository, ResourceBookingMapper resourceBookingMapper, EmailService emailService, StripeService stripeService) {
        this.resourceBookingRepository = resourceBookingRepository;
        this.resourceBookingMapper = resourceBookingMapper;
        this.emailService = emailService;
        this.stripeService = stripeService;
    }

    public List<ResourceBookingDto> getAllBookings() {
        return resourceBookingRepository
                .findAll()
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ResourceBookingDto> getBookingsByUserId(Authentication connectedUser) {
        String userId = connectedUser.getName();
        if (connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER"))) {
            return resourceBookingRepository.findByOrganizerId(userId)
                    .stream()
                    .map(resourceBookingMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return resourceBookingRepository.findByProviderId(userId)
                    .stream()
                    .map(resourceBookingMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    public ResourceBookingDto getBookingById(Long id, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(isUserAuthorized(resourceBooking, connectedUser)) {
            return resourceBookingMapper.toDto(resourceBooking);
        } else {
            throw new IllegalArgumentException("You are not authorized to view this booking");
        }
    }

    @Transactional
    public ResourceBookingDto createBooking(ResourceBookingCreationDto resourceBookingCreationDto, Authentication connectedUser) throws StripeException {
        String organizerId = connectedUser.getName();
        ResourceBooking resourceBooking = resourceBookingMapper.toEntity(resourceBookingCreationDto, organizerId);
        if(!resourceBooking.getEvent().getOrganizerId().equals(organizerId)){
            throw new IllegalArgumentException("You are not authorized to create a booking to this event");
        }
        resourceBookingRepository.save(resourceBooking);

        String bookableName = resourceBooking.getVenue() != null ? resourceBooking.getVenue().getName() : resourceBooking.getOffering().getName();

        String paymentUrl = stripeService.createPaymentLink(
                BigDecimal.valueOf(resourceBooking.getTotalPrice()),
                "USD",
                "Booking for " + bookableName,
                resourceBooking.getId(),
                false
        );

        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String organizerEmail = jwt.getClaimAsString("email");
        emailService.sendBookingPaymentRequest(organizerEmail, bookableName, paymentUrl);
        return resourceBookingMapper.toDto(resourceBooking);
    }

    public ResourceBookingDto updateBooking(Long id, ResourceBookingUpdateDto newBooking, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(isUserAuthorized(resourceBooking, connectedUser)) {
            ResourceBooking b = resourceBookingMapper.updateEntityFromDto(newBooking, resourceBooking);
            resourceBookingRepository.save(b);
            return resourceBookingMapper.toDto(b);
        } else {
            throw new IllegalArgumentException("You are not authorized to update this booking");
        }
    }

    public void cancelBooking(Long id, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(isUserAuthorized(resourceBooking, connectedUser)) {
            resourceBooking.setCancelled(true);
            resourceBooking.setCancellationTime(java.time.LocalDateTime.now());
            resourceBooking.setStatus(ResourceBooking.Status.CANCELLED);
            resourceBookingRepository.save(resourceBooking);

            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
            Jwt jwt = jwtToken.getToken();
            String organizerEmail = jwt.getClaimAsString("email");
            emailService.sendBookingCancellation(resourceBooking, organizerEmail);
        } else {
            throw new IllegalArgumentException("You are not authorized to cancel this booking");
        }
    }

    @Transactional
    public void confirmPayment(Long bookingId, Authentication connectedUser) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String organizerEmail = jwt.getClaimAsString("email");
        String organizerId = connectedUser.getName();

        ResourceBooking resourceBooking = resourceBookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
        if (!resourceBooking.getOrganizerId().equals(organizerId)) {
            throw new IllegalArgumentException("You are not authorized to pay for this booking");
        }
        resourceBooking.setStatus(ResourceBooking.Status.CONFIRMED);
        resourceBookingRepository.save(resourceBooking);
        emailService.sendBookingConfirmation(resourceBooking, organizerEmail);
    }

    private boolean isUserAuthorized(ResourceBooking resourceBooking, Authentication connectedUser) {
        String userId = connectedUser.getName();
        return resourceBooking.getOrganizerId().equals(userId) ||
                (resourceBooking.getVenue() != null && resourceBooking.getVenue().getProviderId().equals(userId)) ||
                (resourceBooking.getOffering() != null && resourceBooking.getOffering().getProviderId().equals(userId));
    }
}

