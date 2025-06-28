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

    public VenueDto createVenue(Venue venue) {
        Venue v = venueRepository.save(venue);
        return venueMapper.toDto(v);
    }

    public VenueDto updateVenue(Long id, Venue newVenue) {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        venue.setName(newVenue.getName());
        venue.setLocation(newVenue.getLocation());
        venue.setType(newVenue.getType());
        venue.setImageUrls(newVenue.getImageUrls());
        venue.setMinCapacity(newVenue.getMinCapacity());
        venue.setMaxCapacity(newVenue.getMaxCapacity());
        venue.setPricePerHour(newVenue.getPricePerHour());
        Venue v = venueRepository.save(venue);
        return venueMapper.toDto(v);
    }

    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Venue not found"));
        venueRepository.delete(venue);
    }
}
