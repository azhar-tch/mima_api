package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.SecurityAgencyRequest;
import com.helpysoft.mima_api.dto.SecurityAgencyResponse;
import com.helpysoft.mima_api.entity.SecurityAgencies;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityAgencyMapper {

    public SecurityAgencies toEntity(SecurityAgencyRequest request) {
        SecurityAgencies agency = new SecurityAgencies();
        agency.setTrackingId(UUID.randomUUID());
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
        agency.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        agency.setTotalEscortsRequested(0);
        agency.setTotalArmedGuardsRequested(0);
        return agency;
    }

    public SecurityAgencyResponse toResponse(SecurityAgencies agency) {
        SecurityAgencyResponse response = new SecurityAgencyResponse();
        response.setTrackingId(agency.getTrackingId());
        response.setAgencyNumber(agency.getAgencyNumber());
        response.setAgencyName(agency.getAgencyName());
        response.setPhoneNumber(agency.getPhoneNumber());
        response.setPhoneNumber2(agency.getPhoneNumber2());
        response.setEmail(agency.getEmail());
        response.setAddress(agency.getAddress());
        response.setCity(agency.getCity());
        response.setCountry(agency.getCountry());
        response.setContactPerson(agency.getContactPerson());
        response.setContactPosition(agency.getContactPosition());
        response.setRegistrationNumber(agency.getRegistrationNumber());
        response.setLicenseNumber(agency.getLicenseNumber());
        response.setWebsite(agency.getWebsite());
        response.setObservations(agency.getObservations());
        response.setIsActive(agency.getIsActive());
        response.setTotalEscortsRequested(agency.getTotalEscortsRequested());
        response.setTotalArmedGuardsRequested(agency.getTotalArmedGuardsRequested());
        response.setCreateDate(agency.getCreateDate());
        return response;
    }
}
