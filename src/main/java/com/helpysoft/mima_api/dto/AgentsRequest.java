package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MarinerStatus;
import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.MaritimeSpecialty;
import com.helpysoft.mima_api.entity.VesselType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentsRequest {
    private String registrationNo;
    private String firstName;
    private String lastName;

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

    private UUID unitTrackingId;
    private Boolean availability;
    private MarinerStatus status;

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
