package com.eventhub.service;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventhub.dto.resourcebooking.ResourceBookingDto;
import com.eventhub.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventhub.mapper.ResourceBookingMapper;
import com.eventhub.model.ResourceBooking;
import com.eventhub.repository.ResourceBookingRepository;
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
public class ResourceBookingService {
    private final ResourceBookingRepository resourceBookingRepository;
    private final ResourceBookingMapper resourceBookingMapper;
    private final EmailService emailService;
    private final StripeService stripeService;
    private final VenueService venueService;

    public ResourceBookingService(ResourceBookingRepository resourceBookingRepository, ResourceBookingMapper resourceBookingMapper, EmailService emailService, StripeService stripeService, VenueService venueService) {
        this.resourceBookingRepository = resourceBookingRepository;
        this.resourceBookingMapper = resourceBookingMapper;
        this.emailService = emailService;
        this.stripeService = stripeService;
        this.venueService = venueService;
    }

    public PageResponse<ResourceBookingDto> getAllBookings(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("bookingTime").descending());
        Page<ResourceBooking> bookings = resourceBookingRepository.findAll(pageable);
        List<ResourceBookingDto> bookingDtos = bookings
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookingDtos,
                bookings.getNumber(),
                bookings.getSize(),
                bookings.getTotalElements(),
                bookings.getTotalPages(),
                bookings.isFirst(),
                bookings.isLast()
        );
    }

    public PageResponse<ResourceBookingDto> getBookingsByOrganizerId(int pageNumber, int pageSize, String organizerId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("bookingTime").descending());
        Page<ResourceBooking> bookings = resourceBookingRepository.findByOrganizerId(organizerId, pageable);
        List<ResourceBookingDto> bookingDtos = bookings
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookingDtos,
                bookings.getNumber(),
                bookings.getSize(),
                bookings.getTotalElements(),
                bookings.getTotalPages(),
                bookings.isFirst(),
                bookings.isLast()
        );
    }

    public PageResponse<ResourceBookingDto> getBookingsByProviderId(int pageNumber, int pageSize, String providerId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("bookingTime").descending());
        Page<ResourceBooking> bookings = resourceBookingRepository.findByProviderId(providerId, pageable);
        List<ResourceBookingDto> bookingDtos = bookings
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookingDtos,
                bookings.getNumber(),
                bookings.getSize(),
                bookings.getTotalElements(),
                bookings.getTotalPages(),
                bookings.isFirst(),
                bookings.isLast()
        );
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
        if(!resourceBooking.getEvent().getCreatedBy().equals(organizerId)){
            throw new IllegalArgumentException("You are not authorized to create a booking to this event");
        }
        if (resourceBooking.getVenue() != null && !venueService.isVenueSuitableForEventType(resourceBooking.getVenue().getId(), resourceBooking.getEvent().getType())) {
            throw new IllegalArgumentException("Selected venue is not suitable for this event type");
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

    // TODO: Transactions
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

