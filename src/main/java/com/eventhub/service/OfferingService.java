package com.eventhub.service;

import com.eventhub.dto.OfferingDto;
import com.eventhub.mapper.OfferingMapper;
import com.eventhub.model.Offering;
import com.eventhub.repository.OfferingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferingService {
    private final OfferingRepository offeringRepository;
    private final OfferingMapper offeringMapper;

    public OfferingService(OfferingRepository offeringRepository, OfferingMapper offeringMapper) {
        this.offeringRepository = offeringRepository;
        this.offeringMapper = offeringMapper;
    }

    public List<OfferingDto> getAllOfferings() {
        return offeringRepository
                .findAll()
                .stream()
                .map(offeringMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<OfferingDto> getAllOfferingsByProvider(String providerId) {
        return offeringRepository
                .findByProviderId(providerId)
                .stream()
                .map(offeringMapper::toDto)
                .collect(Collectors.toList());
    }

    public OfferingDto getOfferingById(Long id, Authentication connectedUser) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER")) ||
                offering.getProviderId().equals(connectedUser.getName())) {
            return offeringMapper.toDto(offering);
        } else {
            throw new IllegalArgumentException("You are not authorized to view this offering");
        }
    }

    public OfferingDto createOffering(OfferingDto offeringDto, Authentication connectedUser) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String providerEmail = jwt.getClaimAsString("email");
        String providerId = connectedUser.getName();
        Offering offering = offeringMapper.toEntity(offeringDto, providerId, providerEmail);
        Offering o = offeringRepository.save(offering);
        return offeringMapper.toDto(o);
    }

    public OfferingDto updateOffering(Long id, OfferingDto newOfferingDto, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if(!offering.getProviderId().equals(providerId)){
            throw new IllegalArgumentException("You are not authorized to update this offering");
        }
        offering.setName(newOfferingDto.getName());
        offering.setPrice(newOfferingDto.getPrice());
        offering.setType(newOfferingDto.getType());
        offering.setOptions(newOfferingDto.getOptions());
        offering.setOfferingAreas(newOfferingDto.getOfferingAreas());
        offering.setAvailabilitySlots(newOfferingDto.getAvailabilitySlots());
        offering.setProviderId(providerId);
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }

    public void deleteOffering(Long id, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if(!offering.getProviderId().equals(providerId)){
            throw new IllegalArgumentException("You are not authorized to delete this offering");
        }
        offeringRepository.delete(offering);
    }

    public List<Offering.Option> getAllOptions(Long id) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        return offering.getOptions();
    }

    public OfferingDto addOption(Long id, Offering.Option option, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if(!offering.getProviderId().equals(providerId)){
            throw new IllegalArgumentException("You are not authorized to add an option to this offering");
        }
        offering.addOption(option);
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }

    public List<Offering.AvailabilitySlot> getAllAvailabilitySlot(Long id) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        return offering.getAvailabilitySlots();
    }

    public OfferingDto addAvailabilitySlot(Long id, LocalDateTime startTime, LocalDateTime endTime, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        if(!offering.getProviderId().equals(providerId)){
            throw new IllegalArgumentException("You are not authorized to add an availability slot to this offering");
        }
        offering.addAvailabilitySlot(new Offering.AvailabilitySlot(startTime, endTime));
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }
}