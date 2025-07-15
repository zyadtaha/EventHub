package com.eventsystem.service;

import com.eventsystem.dto.OfferingDto;
import com.eventsystem.dto.VenueDto;
import com.eventsystem.mapper.VenueMapper;
import com.eventsystem.model.Event.EventType;
import com.eventsystem.model.Offering;
import com.eventsystem.model.Venue;
import com.eventsystem.model.Venue.VenueType;
import com.eventsystem.repository.VenueRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueService(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    public List<VenueDto> getAllVenues() {
        return venueRepository
                .findAll()
                .stream()
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VenueDto> getAllVenuesByProvider(String providerId) {
        return venueRepository
                .findByProviderId(providerId)
                .stream()
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
    }

    public VenueDto getVenueById(Long id, Authentication connectedUser) {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        if (connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER")) ||
                venue.getProviderId().equals(connectedUser.getName())) {
            return venueMapper.toDto(venue);
        } else {
            throw new IllegalArgumentException("You are not authorized to view this venue");
        }
    }

    public VenueDto createVenue(VenueDto venueDto, Authentication connectedUser) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String providerEmail = jwt.getClaimAsString("email");
        String providerId = connectedUser.getName();
        Venue venue = venueMapper.toEntity(venueDto, providerId, providerEmail);
        Venue v = venueRepository.save(venue);
        return venueMapper.toDto(v);
    }

    public VenueDto updateVenue(Long id, VenueDto newVenueDto, String providerId) {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        if(!venue.getProviderId().equals(providerId)){
            throw new IllegalArgumentException("You are not authorized to update this venue");
        }
        venue.setName(newVenueDto.getName());
        venue.setLocation(newVenueDto.getLocation());
        venue.setType(newVenueDto.getType());
        venue.setImageUrls(newVenueDto.getImageUrls());
        venue.setMinCapacity(newVenueDto.getMinCapacity());
        venue.setMaxCapacity(newVenueDto.getMaxCapacity());
        venue.setPricePerHour(newVenueDto.getPricePerHour());
        venue.setProviderId(providerId);
        Venue v = venueRepository.save(venue);
        return venueMapper.toDto(v);
    }

    public void deleteVenue(Long id, String providerId) {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        if(!venue.getProviderId().equals(providerId)){
            throw new IllegalArgumentException("You are not authorized to delete this venue");
        }
        venueRepository.delete(venue);
    }

    private final Map<VenueType, Set<EventType>> eligibilityMap = new HashMap<>(
            Map.of(
                    VenueType.PRIVATE, new HashSet<>(Set.of(EventType.WEDDING, EventType.ENGAGEMENT_PARTY)),
                    VenueType.PUBLIC, new HashSet<>(Set.of(EventType.CONFERENCE, EventType.WORKSHOP))
            )
    );

    public boolean isVenueSuitableForEventType(Long venueId, EventType eventType) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        return eligibilityMap.getOrDefault(venue.getType(), Set.of()).contains(eventType);
    }

    public List<VenueDto> findEligibleVenues(EventType eventType) {
        return venueRepository
                .findAll()
                .stream()
                .filter(v -> eligibilityMap.getOrDefault(v.getType(), Set.of()).contains(eventType))
                .map(venueMapper::toDto)
                .collect(Collectors.toList());
    }

    public void addEligibleEventTypes(VenueType venueType, Set<EventType> eventsToAdd) {
        eligibilityMap.computeIfAbsent(venueType, k -> new HashSet<>()).addAll(eventsToAdd);
    }

    public void removeEligibleEventTypes(VenueType venueType, Set<EventType> eventsToRemove) {
        Set<EventType> currentEvents = eligibilityMap.get(venueType);
        if (currentEvents != null) {
            currentEvents.removeAll(eventsToRemove);
        }
    }
}
