package com.eventhub.service;

import com.eventhub.common.PageResponse;
import com.eventhub.dto.OfferingDto;
import com.eventhub.exception.NotAuthorizedException;
import com.eventhub.exception.NotAuthorizedException.*;
import com.eventhub.exception.NotFoundException;
import com.eventhub.exception.NotFoundException.*;
import com.eventhub.mapper.OfferingMapper;
import com.eventhub.model.Offering;
import com.eventhub.repository.OfferingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.eventhub.constant.ExceptionConstant.*;
import static com.eventhub.constant.ServiceConstant.EMAIL;
import static com.eventhub.constant.ServiceConstant.ROLE_ORGANIZER;
import static com.eventhub.constant.SortConstant.PRICE;

@Service
public class OfferingService {
    private final OfferingRepository offeringRepository;
    private final OfferingMapper offeringMapper;

    public OfferingService(OfferingRepository offeringRepository, OfferingMapper offeringMapper) {
        this.offeringRepository = offeringRepository;
        this.offeringMapper = offeringMapper;
    }

    public PageResponse<OfferingDto> getAllOfferings(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(PRICE).ascending());
        Page<Offering> offerings = offeringRepository.findAll(pageable);
        List<OfferingDto> offeringDtos = offerings
                .stream()
                .map(offeringMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                offeringDtos,
                offerings.getNumber(),
                offerings.getSize(),
                offerings.getTotalElements(),
                offerings.getTotalPages(),
                offerings.isFirst(),
                offerings.isLast()
        );
    }

    public PageResponse<OfferingDto> getAllOfferingsByProvider(int pageNumber, int pageSize, String providerId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(PRICE).ascending());
        Page<Offering> offerings = offeringRepository.findByCreatedBy(providerId, pageable);
        List<OfferingDto> offeringDtos = offerings
                .stream()
                .map(offeringMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                offeringDtos,
                offerings.getNumber(),
                offerings.getSize(),
                offerings.getTotalElements(),
                offerings.getTotalPages(),
                offerings.isFirst(),
                offerings.isLast()
        );
    }

    public OfferingDto getOfferingById(Long id, Authentication connectedUser) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        if(connectedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(ROLE_ORGANIZER)) ||
                offering.getCreatedBy().equals(connectedUser.getName())) {
            return offeringMapper.toDto(offering);
        } else {
            throw new NotAuthorizedException(Action.VIEW, ResourceType.OFFERING, id);
        }
    }

    public OfferingDto createOffering(OfferingDto offeringDto, Authentication connectedUser) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) connectedUser;
        Jwt jwt = jwtToken.getToken();
        String providerEmail = jwt.getClaimAsString(EMAIL);
        Offering offering = offeringMapper.toEntity(offeringDto, providerEmail);
        Offering o = offeringRepository.save(offering);
        return offeringMapper.toDto(o);
    }

    public OfferingDto updateOffering(Long id, OfferingDto newOfferingDto, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        if(!offering.getCreatedBy().equals(providerId)){
            throw new NotAuthorizedException(Action.UPDATE, ResourceType.OFFERING, id);
        }
        offering.setName(newOfferingDto.getName());
        offering.setPrice(newOfferingDto.getPrice());
        offering.setType(newOfferingDto.getType());
        offering.setOptions(newOfferingDto.getOptions());
        offering.setOfferingAreas(newOfferingDto.getOfferingAreas());
        offering.setAvailabilitySlots(newOfferingDto.getAvailabilitySlots());
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }

    public void deleteOffering(Long id, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        if(!offering.getCreatedBy().equals(providerId)){
            throw new NotAuthorizedException(Action.DELETE, ResourceType.OFFERING, id);
        }
        offeringRepository.delete(offering);
    }

    public List<Offering.Option> getAllOptions(Long id) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        return offering.getOptions();
    }

    public OfferingDto addOption(Long id, Offering.Option option, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        if(!offering.getCreatedBy().equals(providerId)){
            throw new NotAuthorizedException(NOT_AUTHORIZED_TO_ADD_AVAILABILITY_SLOT_MESSAGE);
        }
        offering.addOption(option);
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }

    public List<Offering.AvailabilitySlot> getAllAvailabilitySlot(Long id) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        return offering.getAvailabilitySlots();
    }

    public OfferingDto addAvailabilitySlot(Long id, LocalDateTime startTime, LocalDateTime endTime, String providerId) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new NotFoundException(EntityType.OFFERING));
        if(!offering.getCreatedBy().equals(providerId)){
            throw new NotAuthorizedException(NOT_AUTHORIZED_TO_ADD_OPTION_MESSAGE);
        }
        offering.addAvailabilitySlot(new Offering.AvailabilitySlot(startTime, endTime));
        offeringRepository.save(offering);
        return offeringMapper.toDto(offering);
    }
}