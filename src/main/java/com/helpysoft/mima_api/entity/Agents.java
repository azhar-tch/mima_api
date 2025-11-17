package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Agents extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String registrationNo;

    // Informations maritimes spécifiques
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritimeRank maritimeRank;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritimeSpecialty specialty;

    @Column(unique = true)
    private String seafarerBookNumber; // Numéro de livret maritime

    @Column(unique = true)
    private String seamanLicenseNumber; // Numéro de licence de marin

    private LocalDate medicalCertificateExpiry; // Date d'expiration du certificat médical

    @Column(length = 500)
    private String maritimeQualifications; // Qualifications maritimes (STCW, etc.)

    @Enumerated(EnumType.STRING)
    private VesselType qualifiedVesselType; // Type de navire sur lequel il est qualifié

    private Integer yearsOfSeaExperience; // Années d'expérience en mer

    private LocalDate lastSeaDutyDate; // Dernière date de service en mer

    @Column(length = 500)
    private String certifications; // Certificats maritimes (séparés par virgules)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Units unit;

    @Column(nullable = false)
    private Boolean availability = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarinerStatus status = MarinerStatus.PERMISSION;

    @Column(nullable = false)
    private char sex;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String email;

    @Column(length = 20)
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
