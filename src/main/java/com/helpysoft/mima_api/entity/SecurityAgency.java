package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

/**
 * Entité pour la gestion des agences de sécurité
 * Basé sur le cahier des charges - Table Agence de sécurité
 * Les agences de sécurité effectuent les demandes d'escorte et de garde armée
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "security_agencies")
public class SecurityAgency extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro d'identification unique de l'agence
     * Attribué par ordre croissant
     */
    @Column(name = "agency_number", nullable = false, unique = true, length = 20)
    private String agencyNumber;

    @Column(name = "agency_name", nullable = false, length = 200)
    private String agencyName;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber; // num_tel_agence

    @Column(name = "phone_number_2", length = 50)
    private String phoneNumber2; // Numéro secondaire

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 300)
    private String address; // adress_agence

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "contact_person", length = 150)
    private String contactPerson; // Personne de contact

    @Column(name = "contact_position", length = 100)
    private String contactPosition; // Poste de la personne de contact

    @Column(name = "registration_number", length = 100)
    private String registrationNumber; // Numéro d'immatriculation

    @Column(name = "license_number", length = 100)
    private String licenseNumber; // Numéro de licence

    @Column(name = "website", length = 200)
    private String website;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "is_active")
    private Boolean isActive = true; // Agence active ou inactive

    /**
     * Nombre total d'escortes demandées
     */
    @Column(name = "total_escorts_requested")
    private Integer totalEscortsRequested = 0;

    /**
     * Nombre total de gardes armées demandées
     */
    @Column(name = "total_armed_guards_requested")
    private Integer totalArmedGuardsRequested = 0;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
