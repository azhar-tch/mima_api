package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.MarinerStatus;
import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.MaritimeSpecialty;
import com.helpysoft.mima_api.entity.VesselType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentsResponse {
    private UUID trackingId;
    private String firstName;
    private String lastName;
    private String registrationNo;

    // Informations maritimes
    private MaritimeRank maritimeRank;
    private MaritimeSpecialty specialty;
    private String seafarerBookNumber;
    private String seamanLicenseNumber;
    private LocalDate medicalCertificateExpiry;
    private String maritimeQualifications;
    private VesselType qualifiedVesselType;
    private Integer yearsOfSeaExperience;
    private LocalDate lastSeaDutyDate;
    private String certifications;

    private String unitName;
    private UUID unitTrackingId;
    private Boolean availability;
    private MarinerStatus status;
    private LocalDateTime createDate;

    // Informations personnelles
    private char sex;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private String nationality;
    private String city;
    private String emergencyContact;
    private String maritalStatus;
    private LocalDate recruitmentDate;
    private LocalDate contractEndDate;
    private String idCardNumber;
    private String passportNumber;
    private LocalDate idExpiryDate;
    private String insuranceNumber;
    private String bankAccount;
}
