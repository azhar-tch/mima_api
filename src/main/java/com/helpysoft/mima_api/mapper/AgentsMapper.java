package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.MarinerStatus;
import com.helpysoft.mima_api.entity.Units;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentsMapper {

    public Agents toEntity(AgentsRequest request, Units unit) {
        Agents agent = new Agents();
        agent.setTrackingId(UUID.randomUUID());
        agent.setFirstName(request.getFirstName());
        agent.setLastName(request.getLastName());
        agent.setRegistrationNo(request.getRegistrationNo());

        // Informations maritimes
        agent.setMaritimeRank(request.getMaritimeRank());
        agent.setSpecialty(request.getSpecialty());
        agent.setSeafarerBookNumber(request.getSeafarerBookNumber());
        agent.setSeamanLicenseNumber(request.getSeamanLicenseNumber());
        agent.setMedicalCertificateExpiry(request.getMedicalCertificateExpiry());
        agent.setMaritimeQualifications(request.getMaritimeQualifications());
        agent.setQualifiedVesselType(request.getQualifiedVesselType());
        agent.setYearsOfSeaExperience(request.getYearsOfSeaExperience());
        agent.setLastSeaDutyDate(request.getLastSeaDutyDate());
        agent.setCertifications(request.getCertifications());

        agent.setUnit(unit);
        agent.setAvailability(request.getAvailability() != null ? request.getAvailability() : true);
        agent.setStatus(request.getStatus() != null ? request.getStatus() : MarinerStatus.PERMISSION);

        // Informations personnelles
        if (request.getSex() == null) {
            throw new IllegalArgumentException("Le sexe est requis");
        }
        agent.setSex(request.getSex());
        agent.setDateOfBirth(request.getDateOfBirth());
        agent.setEmail(request.getEmail());
        agent.setPhoneNumber(request.getPhoneNumber());
        agent.setNationality(request.getNationality());
        agent.setCity(request.getCity());
        agent.setEmergencyContact(request.getEmergencyContact());
        agent.setMaritalStatus(request.getMaritalStatus());
        agent.setRecruitmentDate(request.getRecruitmentDate());
        agent.setContractEndDate(request.getContractEndDate());
        agent.setIdCardNumber(request.getIdCardNumber());
        agent.setPassportNumber(request.getPassportNumber());
        agent.setIdExpiryDate(request.getIdExpiryDate());
        agent.setInsuranceNumber(request.getInsuranceNumber());
        agent.setBankAccount(request.getBankAccount());

        return agent;
    }

    public AgentsResponse toResponse(Agents agent) {
        AgentsResponse response = new AgentsResponse();
        response.setTrackingId(agent.getTrackingId());
        response.setFirstName(agent.getFirstName());
        response.setLastName(agent.getLastName());
        response.setRegistrationNo(agent.getRegistrationNo());

        // Informations maritimes
        response.setMaritimeRank(agent.getMaritimeRank());
        response.setSpecialty(agent.getSpecialty());
        response.setSeafarerBookNumber(agent.getSeafarerBookNumber());
        response.setSeamanLicenseNumber(agent.getSeamanLicenseNumber());
        response.setMedicalCertificateExpiry(agent.getMedicalCertificateExpiry());
        response.setMaritimeQualifications(agent.getMaritimeQualifications());
        response.setQualifiedVesselType(agent.getQualifiedVesselType());
        response.setYearsOfSeaExperience(agent.getYearsOfSeaExperience());
        response.setLastSeaDutyDate(agent.getLastSeaDutyDate());
        response.setCertifications(agent.getCertifications());

        response.setUnitName(agent.getUnit() != null ? agent.getUnit().getName() : null);
        response.setUnitTrackingId(agent.getUnit() != null ? agent.getUnit().getTrackingId() : null);
        response.setAvailability(agent.getAvailability());
        response.setStatus(agent.getStatus());
        response.setCreateDate(agent.getCreateDate());

        // Informations personnelles
        response.setSex(agent.getSex());
        response.setDateOfBirth(agent.getDateOfBirth());
        response.setEmail(agent.getEmail());
        response.setPhoneNumber(agent.getPhoneNumber());
        response.setNationality(agent.getNationality());
        response.setCity(agent.getCity());
        response.setEmergencyContact(agent.getEmergencyContact());
        response.setMaritalStatus(agent.getMaritalStatus());
        response.setRecruitmentDate(agent.getRecruitmentDate());
        response.setContractEndDate(agent.getContractEndDate());
        response.setIdCardNumber(agent.getIdCardNumber());
        response.setPassportNumber(agent.getPassportNumber());
        response.setIdExpiryDate(agent.getIdExpiryDate());
        response.setInsuranceNumber(agent.getInsuranceNumber());
        response.setBankAccount(agent.getBankAccount());

        return response;
    }
}
