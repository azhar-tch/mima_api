package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entité pour la gestion des missions de garde armée à bord des navires de commerce
 * Basé sur le cahier des charges - Table Mission Navire
 * Enregistrement en deux phases: embarquement puis débarquement
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "armed_guard_missions")
public class ArmedGuardMission extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro d'identification automatique de la mission
     */
    @Column(name = "mission_number", nullable = false, unique = true, length = 50)
    private String missionNumber;

    /**
     * Navire de commerce où la garde armée est embarquée
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commercial_ship_id", nullable = false)
    private CommercialShip commercialShip;

    /**
     * Agence de sécurité qui a effectué la demande
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_agency_id", nullable = false)
    private SecurityAgency securityAgency;

    /**
     * Date et heure d'embarquement
     */
    @Column(name = "embarkation_date", nullable = false)
    private LocalDateTime embarkationDate;

    /**
     * Date et heure de débarquement
     */
    @Column(name = "disembarkation_date")
    private LocalDateTime disembarkationDate;

    /**
     * Port d'embarquement
     */
    @Column(name = "embarkation_port", length = 100)
    private String embarkationPort;

    /**
     * Port de débarquement
     */
    @Column(name = "disembarkation_port", length = 100)
    private String disembarkationPort;

    /**
     * Nombre de jours de garde (calculé automatiquement)
     * Note: Si embarquement et débarquement le même jour, compter avec 1 jour de différence
     */
    @Column(name = "days_count")
    private Integer daysCount;

    /**
     * Nombre de militaires embarqués
     */
    @Column(name = "personnel_count")
    private Integer personnelCount;

    /**
     * Zone de patrouille/surveillance
     */
    @Column(name = "patrol_zone", length = 200)
    private String patrolZone;

    /**
     * Statut de la mission
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MissionStatus status = MissionStatus.IN_PROGRESS;

    /**
     * Incidents signalés pendant la mission
     */
    @Column(name = "incidents", length = 1000)
    private String incidents;

    /**
     * Observations
     */
    @Column(name = "observations", length = 1000)
    private String observations;

    /**
     * Personnel militaire embarqué
     */
    @OneToMany(mappedBy = "armedGuardMission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArmedGuardPersonnel> guardPersonnel = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
        if (missionNumber == null) {
            missionNumber = "GA-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateDaysCount();
    }

    /**
     * Calcule le nombre de jours de la mission
     * Si embarquement et débarquement le même jour, compte 1 jour
     */
    public void calculateDaysCount() {
        if (embarkationDate != null && disembarkationDate != null) {
            long days = java.time.Duration.between(embarkationDate, disembarkationDate).toDays();
            // Si même jour, compter au moins 1 jour selon le cahier des charges
            this.daysCount = days == 0 ? 1 : (int) days;
        }
    }

    /**
     * Calcule la durée en heures
     */
    public Long getDurationInHours() {
        if (embarkationDate != null && disembarkationDate != null) {
            return java.time.Duration.between(embarkationDate, disembarkationDate).toHours();
        }
        return null;
    }
}
