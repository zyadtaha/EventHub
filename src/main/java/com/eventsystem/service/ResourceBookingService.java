package com.eventsystem.service;

import com.eventsystem.dto.resourcebooking.ResourceBookingCreationDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingDto;
import com.eventsystem.dto.resourcebooking.ResourceBookingUpdateDto;
import com.eventsystem.mapper.ResourceBookingMapper;
import com.eventsystem.model.ResourceBooking;
import com.eventsystem.repository.ResourceBookingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceBookingService {
    ResourceBookingRepository resourceBookingRepository;
    ResourceBookingMapper resourceBookingMapper;

    public ResourceBookingService(ResourceBookingRepository resourceBookingRepository, ResourceBookingMapper resourceBookingMapper) {
        this.resourceBookingRepository = resourceBookingRepository;
        this.resourceBookingMapper = resourceBookingMapper;
    }

    public List<ResourceBookingDto> getAllBookings() {
        return resourceBookingRepository
                .findAll()
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ResourceBookingDto> getAllBookingsByOrganizer(Authentication connectedUser) {
        return resourceBookingRepository
                .findByOrganizerId(connectedUser.getName())
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ResourceBookingDto> getAllBookingsByProvider(Authentication connectedUser) {
        return resourceBookingRepository
                .findByProviderId(connectedUser.getName())
                .stream()
                .map(resourceBookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResourceBookingDto getBookingById(Long id, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER") && resourceBooking.getOrganizerId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_VENUE_PROVIDER") && resourceBooking.getVenue() != null && resourceBooking.getVenue().getProviderId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OFFERING_PROVIDER") && resourceBooking.getOffering() != null && resourceBooking.getOffering().getProviderId().equals(connectedUser.getName()))
        ) {
            return resourceBookingMapper.toDto(resourceBooking);
        } else {
            throw new IllegalArgumentException("You are not authorized to view this booking");
        }
    }

    public ResourceBookingDto createBooking(ResourceBookingCreationDto resourceBookingCreationDto, String organizerId) {
        ResourceBooking resourceBooking = resourceBookingMapper.toEntity(resourceBookingCreationDto, organizerId);
        if(!resourceBooking.getEvent().getOrganizerId().equals(organizerId)){
            throw new IllegalArgumentException("You are not authorized to create a booking to this event");
        }
        resourceBookingRepository.save(resourceBooking);
        return resourceBookingMapper.toDto(resourceBooking);
    }

    public ResourceBookingDto updateBooking(Long id, ResourceBookingUpdateDto newBooking, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER") && resourceBooking.getOrganizerId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_VENUE_PROVIDER") && resourceBooking.getVenue() != null && resourceBooking.getVenue().getProviderId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OFFERING_PROVIDER") && resourceBooking.getOffering() != null && resourceBooking.getOffering().getProviderId().equals(connectedUser.getName()))
        ) {
            ResourceBooking b = resourceBookingMapper.updateFromDtoToEntity(newBooking, resourceBooking);
            resourceBookingRepository.save(b);
            return resourceBookingMapper.toDto(b);
        } else {
            throw new IllegalArgumentException("You are not authorized to update this booking");
        }
    }

    // TODO: should it be cancelBooking instead of deleteBooking?
    public void deleteBooking(Long id, Authentication connectedUser) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER") && resourceBooking.getOrganizerId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_VENUE_PROVIDER") && resourceBooking.getVenue() != null && resourceBooking.getVenue().getProviderId().equals(connectedUser.getName())) ||
                connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OFFERING_PROVIDER") && resourceBooking.getOffering() != null && resourceBooking.getOffering().getProviderId().equals(connectedUser.getName()))
        ) {
            resourceBookingRepository.delete(resourceBooking);
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this booking");
        }
    }
}

