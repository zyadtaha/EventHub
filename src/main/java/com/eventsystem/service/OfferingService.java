package com.eventsystem.service;

import com.eventsystem.dto.OfferingDto;
import com.eventsystem.mapper.OfferingMapper;
import com.eventsystem.model.Offering;
import com.eventsystem.repository.OfferingRepository;
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

    public OfferingDto createOffering(Offering offering) {
        Offering o = offeringRepository.save(offering);
        return offeringMapper.toDto(o);
    }

    public OfferingDto updateOffering(Long id, Offering newOffering) {
        Offering o = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        o.setName(newOffering.getName());
        o.setPrice(newOffering.getPrice());
        o.setType(newOffering.getType());
        o.setOptions(newOffering.getOptions());
        o.setOfferingAreas(newOffering.getOfferingAreas());
        o.setAvailabilitySlots(newOffering.getAvailabilitySlots());
        return offeringMapper.toDto(o);
    }

    public void deleteOffering(Long id) {
        offeringRepository.deleteById(id);
    }

    public List<Offering.Option> getAllOptions(Long id) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        return offering.getOptions();
    }

    public OfferingDto addOption(Long id, Offering.Option option) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        offering.addOption(option);
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }

    public List<Offering.AvailabilitySlot> getAllAvailabilitySlot(Long id) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        return offering.getAvailabilitySlots();
    }

    public OfferingDto addAvailabilitySlot(Long id, LocalDateTime startTime, LocalDateTime endTime) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        offering.addAvailabilitySlot(new Offering.AvailabilitySlot(startTime, endTime));
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }
}