package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.SecurityAgencyRequest;
import com.helpysoft.mima_api.dto.SecurityAgencyResponse;

import java.util.List;
import java.util.UUID;

public interface SecurityAgencyService {
    SecurityAgencyResponse create(SecurityAgencyRequest request);
    SecurityAgencyResponse update(UUID trackingId, SecurityAgencyRequest request);
    SecurityAgencyResponse findByTrackingId(UUID trackingId);
    SecurityAgencyResponse findByAgencyNumber(String agencyNumber);
    List<SecurityAgencyResponse> findByAgencyName(String agencyName);
    List<SecurityAgencyResponse> findActiveAgencies();
    List<SecurityAgencyResponse> findTopAgenciesByEscorts();
    List<SecurityAgencyResponse> findTopAgenciesByArmedGuards();
    List<SecurityAgencyResponse> findAll();
    List<SecurityAgencyResponse> searchSecurityAgencies(String searchTerm);
    void delete(UUID trackingId);
}
