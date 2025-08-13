package com.eventhub.service;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventhub.dto.resourcebooking.ResourceBookingDto;
import com.eventhub.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventhub.exception.NotAuthorizedException;
import com.eventhub.exception.NotAuthorizedException.*;
import com.eventhub.exception.NotFoundException;
import com.eventhub.exception.NotFoundException.*;
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

import static com.eventhub.constant.ExceptionConstant.NOT_SUITABLE_WITH_EVENT_TYPE;
import static com.eventhub.constant.ServiceConstant.*;
import static com.eventhub.constant.SortConstant.CREATED_AT;

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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(CREATED_AT).descending());
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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(CREATED_AT).descending());
        Page<ResourceBooking> bookings = resourceBookingRepository.findByCreatedBy(organizerId, pageable);
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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(CREATED_AT).descending());
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
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.BOOKING));
        if(isUserAuthorized(resourceBooking, connectedUser)) {
            return resourceBookingMapper.toDto(resourceBooking);
        } else {
            throw new NotAuthorizedException(Action.VIEW, ResourceType.BOOKING, id);
        }
    }

    @Transactional
    public ResourceBookingDto createBooking(ResourceBookingCreationDto resourceBookingCreationDto, Authentication connectedUser) throws StripeException {
        String organizerId = connectedUser.getName();
        ResourceBooking resourceBooking = resourceBookingMapper.toEntity(resourceBookingCreationDto);
        if(!resourceBooking.getEvent().getCreatedBy().equals(organizerId)){
            throw new NotAuthorizedException(Action.CREATE, ResourceType.BOOKING);
        }
        if (resourceBooking.getVenue() != null && !venueService.isVenueSuitableForEventType(resourceBooking.getVenue().getId(), resourceBooking.getEvent().getType())) {
            throw new IllegalStateException(NOT_SUITABLE_WITH_EVENT_TYPE);
        }
        resourceBookingRepository.save(resourceBooking);

        String bookableName = resourceBooking.getVenue() != null ? resourceBooking.getVenue().getName() : resourceBooking.getOffering().getName();

        String paymentUrl = stripeService.createPaymentLink(
                BigDecimal.valueOf(resourceBooking.getTotalPrice()),
                PAYMENT_CURRENCY,
                String.format(BOOKING_FOR, bookableName),
                resourceBooking.getId(),
                false
        );

        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String organizerEmail = jwt.getClaimAsString(EMAIL);
        emailService.sendBookingPaymentRequest(organizerEmail, bookableName, paymentUrl);
        return resourceBookingMapper.toDto(resourceBooking);
    }

    // TODO: Transactions
    public ResourceBookingDto updateBooking(Long id, ResourceBookingUpdateDto newBooking, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.BOOKING));
        if(isUserAuthorized(resourceBooking, connectedUser)) {
            ResourceBooking b = resourceBookingMapper.updateEntityFromDto(newBooking, resourceBooking);
            resourceBookingRepository.save(b);
            return resourceBookingMapper.toDto(b);
        } else {
            throw new NotAuthorizedException(Action.UPDATE, ResourceType.BOOKING, id);
        }
    }

    public void cancelBooking(Long id, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.BOOKING));
        if(isUserAuthorized(resourceBooking, connectedUser)) {
            resourceBooking.setCancelled(true);
            resourceBooking.setCancellationTime(java.time.LocalDateTime.now());
            resourceBooking.setStatus(ResourceBooking.Status.CANCELLED);
            resourceBookingRepository.save(resourceBooking);

            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
            Jwt jwt = jwtToken.getToken();
            String organizerEmail = jwt.getClaimAsString(EMAIL);
            emailService.sendBookingCancellation(resourceBooking, organizerEmail);
        } else {
            throw new NotAuthorizedException(Action.CANCEL, ResourceType.BOOKING, id);
        }
    }

    @Transactional
    public void confirmPayment(Long bookingId, Authentication connectedUser) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String organizerEmail = jwt.getClaimAsString(EMAIL);
        String organizerId = connectedUser.getName();

        ResourceBooking resourceBooking = resourceBookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(EntityType.BOOKING));
        if (!resourceBooking.getCreatedBy().equals(organizerId)) {
            throw new NotAuthorizedException(Action.PAY, ResourceType.BOOKING, bookingId);
        }
        resourceBooking.setStatus(ResourceBooking.Status.CONFIRMED);
        resourceBookingRepository.save(resourceBooking);
        emailService.sendBookingConfirmation(resourceBooking, organizerEmail);
    }

    private boolean isUserAuthorized(ResourceBooking resourceBooking, Authentication connectedUser) {
        String userId = connectedUser.getName();
        return resourceBooking.getCreatedBy().equals(userId) ||
                (resourceBooking.getVenue() != null && resourceBooking.getVenue().getCreatedBy().equals(userId)) ||
                (resourceBooking.getOffering() != null && resourceBooking.getOffering().getCreatedBy().equals(userId));
    }
}

