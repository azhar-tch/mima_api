package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.SecurityAgencyRequest;
import com.helpysoft.mima_api.dto.SecurityAgencyResponse;
import com.helpysoft.mima_api.entity.SecurityAgencies;
import com.helpysoft.mima_api.mapper.SecurityAgencyMapper;
import com.helpysoft.mima_api.repository.SecurityAgencyRepository;
import com.helpysoft.mima_api.service.SecurityAgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityAgencyServiceImpl implements SecurityAgencyService {

    private final SecurityAgencyRepository securityAgencyRepository;
    private final SecurityAgencyMapper securityAgencyMapper;

    @Override
    public SecurityAgencyResponse create(SecurityAgencyRequest request) {
        SecurityAgencies agency = securityAgencyMapper.toEntity(request);
        SecurityAgencies savedAgency = securityAgencyRepository.save(agency);
        return securityAgencyMapper.toResponse(savedAgency);
    }

    @Override
    public SecurityAgencyResponse update(UUID trackingId, SecurityAgencyRequest request) {
        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Security agency not found with trackingId: " + trackingId));

        agency.setAgencyNumber(request.getAgencyNumber());
        agency.setAgencyName(request.getAgencyName());
        agency.setPhoneNumber(request.getPhoneNumber());
        agency.setPhoneNumber2(request.getPhoneNumber2());
        agency.setEmail(request.getEmail());
        agency.setAddress(request.getAddress());
        agency.setCity(request.getCity());
        agency.setCountry(request.getCountry());
        agency.setContactPerson(request.getContactPerson());
        agency.setContactPosition(request.getContactPosition());
        agency.setRegistrationNumber(request.getRegistrationNumber());
        agency.setLicenseNumber(request.getLicenseNumber());
        agency.setWebsite(request.getWebsite());
        agency.setObservations(request.getObservations());
        agency.setIsActive(request.getIsActive());

        SecurityAgencies updatedAgency = securityAgencyRepository.save(agency);
        return securityAgencyMapper.toResponse(updatedAgency);
    }

    @Override
    @Transactional(readOnly = true)
    public SecurityAgencyResponse findByTrackingId(UUID trackingId) {
        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Security agency not found with trackingId: " + trackingId));
        return securityAgencyMapper.toResponse(agency);
    }

    @Override
    @Transactional(readOnly = true)
    public SecurityAgencyResponse findByAgencyNumber(String agencyNumber) {
        SecurityAgencies agency = securityAgencyRepository.findByAgencyNumber(agencyNumber)
                .orElseThrow(() -> new RuntimeException("Security agency not found with agency number: " + agencyNumber));
        return securityAgencyMapper.toResponse(agency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityAgencyResponse> findByAgencyName(String agencyName) {
        return securityAgencyRepository.findByAgencyNameContainingIgnoreCase(agencyName)
                .stream()
                .map(securityAgencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityAgencyResponse> findActiveAgencies() {
        return securityAgencyRepository.findByIsActiveTrue()
                .stream()
                .map(securityAgencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityAgencyResponse> findTopAgenciesByEscorts() {
        return securityAgencyRepository.findTopAgenciesByEscorts()
                .stream()
                .map(securityAgencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityAgencyResponse> findTopAgenciesByArmedGuards() {
        return securityAgencyRepository.findTopAgenciesByArmedGuards()
                .stream()
                .map(securityAgencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityAgencyResponse> findAll() {
        return securityAgencyRepository.findAll()
                .stream()
                .map(securityAgencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityAgencyResponse> searchSecurityAgencies(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return securityAgencyRepository.searchSecurityAgencies(searchTerm)
                .stream()
                .map(securityAgencyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Security agency not found with trackingId: " + trackingId));
        securityAgencyRepository.delete(agency);
    }
}
