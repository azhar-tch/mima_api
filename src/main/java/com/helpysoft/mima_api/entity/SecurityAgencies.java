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
public class SecurityAgencies extends AuditTable implements Serializable {

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
    @Column(nullable = false, unique = true, length = 20)
    private String agencyNumber;

    @Column(nullable = false, length = 200)
    private String agencyName;

    @Column(length = 50)
    private String phoneNumber; // num_tel_agence

    @Column(length = 50)
    private String phoneNumber2; // Numéro secondaire

    @Column(length = 100)
    private String email;

    @Column(length = 300)
    private String address; // adress_agence

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @Column(length = 150)
    private String contactPerson; // Personne de contact

    @Column(length = 100)
    private String contactPosition; // Poste de la personne de contact

    @Column(length = 100)
    private String registrationNumber; // Numéro d'immatriculation

    @Column(length = 100)
    private String licenseNumber; // Numéro de licence

    @Column(length = 200)
    private String website;

    @Column(length = 1000)
    private String observations;

    @Column
    private Boolean isActive = true; // Agence active ou inactive

    /**
     * Nombre total d'escortes demandées
     */
    @Column
    private Integer totalEscortsRequested = 0;

    /**
     * Nombre total de gardes armées demandées
     */
    @Column
    private Integer totalArmedGuardsRequested = 0;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
