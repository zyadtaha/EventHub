package com.eventsystem.service;

import com.eventsystem.dto.VenueDto;
import com.eventsystem.mapper.VenueMapper;
import com.eventsystem.model.Venue;
import com.eventsystem.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public VenueDto createVenue(VenueDto venueDto, String providerId) {
        Venue venue = venueMapper.toEntity(venueDto, providerId);
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
}
