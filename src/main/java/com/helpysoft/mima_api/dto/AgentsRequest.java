package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MarinerStatus;
import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.MaritimeSpecialty;
import com.helpysoft.mima_api.entity.VesselType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentsRequest {
    @NotBlank(message = "Le numéro d'enregistrement est requis")
    private String registrationNo;

    @NotBlank(message = "Le prénom est requis")
    private String firstName;

    @NotBlank(message = "Le nom est requis")
    private String lastName;

    // Informations maritimes
    @NotNull(message = "Le grade maritime est requis")
    private MaritimeRank maritimeRank;

    @NotNull(message = "La spécialité maritime est requise")
    private MaritimeSpecialty specialty;
    private String seafarerBookNumber;
    private String seamanLicenseNumber;
    private LocalDate medicalCertificateExpiry;
    private String maritimeQualifications;
    private VesselType qualifiedVesselType;
    private Integer yearsOfSeaExperience;
    private LocalDate lastSeaDutyDate;
    private String certifications;

    @NotNull(message = "L'unité est requise")
    private UUID unitTrackingId;
    private Boolean availability;
    private MarinerStatus status;

    // Informations personnelles
    @NotNull(message = "Le sexe est requis")
    private Character sex;
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
