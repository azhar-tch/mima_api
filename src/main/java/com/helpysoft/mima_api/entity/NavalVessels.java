package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entité pour la gestion des moyens navals de la marine nationale
 * Basé sur le cahier des charges - Table Moyens navals MN
 * Comprend: Patrouilleurs (PHM), Vedettes (VDT), Embarcations rapides (EMB)
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "naval_vessel")
public class NavalVessels extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro d'identification unique du moyen naval
     * Format selon cahier des charges:
     * - Pour moyens actuels: 761, 763, 764, 765, 100, 101, 102, etc.
     * - Pour nouveaux moyens: XXXXX (Année mise en service + numéro ordre)
     * Exemple: 20091 = mis en service en 2009, 1er moyen de l'année
     */
    @Column(nullable = false, unique = true)
    private String vesselNumber;

    /**
     * Type de moyen naval
     * PHM = Patrouilleur Hauturier Maritime
     * VDT = Vedette
     * VDT rapide = Vedette rapide
     * VDT côtière = Vedette côtière
     * EMB rapide = Embarcation rapide
     * EMB rade = Embarcation de rade
     * Salle = Salle d'opérations (COM LOME, COM GOUMOU)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NavalVesselType vesselType;

    @Column(nullable = false, length = 50)
    private String vesselName;

    @Column(length = 20)
    private String hullNumber; // Numéro de coque

    @Column
    private Integer yearCommissioned; // Année de mise en service

    @Column
    private LocalDate dateCommissioned; // Date de mise en service

    @Column
    private LocalDate dateDecommissioned; // Date de mise en réforme

    @Column
    private Double length; // Longueur en mètres

    @Column
    private Double width; // Largeur en mètres

    @Column
    private Double draft; // Tirant d'eau en mètres

    @Column
    private Double displacement; // Déplacement en tonnes

    @Column
    private Double maxSpeed; // Vitesse maximale en nœuds

    @Column
    private Integer crewCapacity; // Capacité d'équipage

    @Column
    private Double fuelCapacity; // Capacité carburant en litres

    @Column(name = "`range`")
    private Double range; // Autonomie en miles nautiques

    @Column(length = 500)
    private String armament; // Armement

    @Column(length = 500)
    private String electronics; // Électronique (radar, sonar, etc.)

    @Column(length = 100)
    private String engineType; // Type de moteur

    @Column
    private Integer enginePower; // Puissance moteur en CV

    @Column(length = 100)
    private String homePort; // Port d'attache

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NavalVesselStatus operationalStatus = NavalVesselStatus.OPERATIONAL;

    @Column(length = 100)
    private String currentLocation; // Position actuelle

    @Column(length = 200)
    private String currentMission; // Mission en cours

    @Column
    private LocalDate lastMaintenanceDate; // Dernière maintenance

    @Column
    private LocalDate nextMaintenanceDate; // Prochaine maintenance

    @Column
    private Double totalOperationalHours; // Heures de navigation totales

    @Column(length = 1000)
    private String observations; // Observations

    @Column
    private Boolean isActive = true; // Moyen actif ou réformé

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
