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
 * Entité pour la gestion des missions d'escorte des navires
 * Basé sur le cahier des charges - Table Escorte Navire
 * Gestion des opérations d'escorte des navires de commerce
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "escort_missions")
public class EscortMission extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro d'identification automatique de la mission d'escorte
     */
    @Column(name = "mission_number", nullable = false, unique = true, length = 50)
    private String missionNumber;

    /**
     * Navire escorté
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commercial_ship_id", nullable = false)
    private CommercialShip commercialShip;

    /**
     * Agence de sécurité qui a effectué la demande d'escorte
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_agency_id", nullable = false)
    private SecurityAgency securityAgency;

    /**
     * Patrouilleur escorteur principal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "naval_vessel_id", nullable = false)
    private NavalVessel navalVessel;

    /**
     * Commandant du patrouilleur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commander_id", nullable = false)
    private Agents commander;

    /**
     * Grade du commandant au moment de la mission
     */
    @Column(name = "commander_rank", length = 50)
    private String commanderRank;

    /**
     * Autre navire escorte (optionnel - pour escortes simultanées)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_vessel_id")
    private NavalVessel secondaryVessel;

    /**
     * Vedettes (optionnel - pour missions avec vedettes)
     */
    @Column(name = "vedettes", length = 200)
    private String vedettes; // Liste de vedettes séparées par virgules

    /**
     * Date et heure de début de mission
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * Date et heure de fin de mission
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * Type d'escorte
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "escort_type")
    private EscortType escortType;

    /**
     * Point de départ
     */
    @Column(name = "departure_point", length = 200)
    private String departurePoint;

    /**
     * Point d'arrivée
     */
    @Column(name = "arrival_point", length = 200)
    private String arrivalPoint;

    /**
     * Distance parcourue en miles nautiques
     */
    @Column(name = "distance")
    private Double distance;

    /**
     * Zone d'escorte
     */
    @Column(name = "escort_zone", length = 200)
    private String escortZone;

    /**
     * Statut de la mission
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MissionStatus status = MissionStatus.IN_PROGRESS;

    /**
     * Incidents rencontrés
     */
    @Column(name = "incidents", length = 1000)
    private String incidents;

    /**
     * Observations
     */
    @Column(name = "observations", length = 1000)
    private String observations;

    /**
     * Personnel de l'équipage
     */
    @OneToMany(mappedBy = "escortMission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EscortPersonnel> crew = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
        if (missionNumber == null) {
            missionNumber = "ESC-" + System.currentTimeMillis();
        }
    }

    /**
     * Calcule la durée de la mission en heures
     */
    public Long getDurationInHours() {
        if (startDate != null && endDate != null) {
            return java.time.Duration.between(startDate, endDate).toHours();
        }
        return null;
    }

    /**
     * Calcule le nombre de jours de la mission (arrondi supérieur)
     */
    public int getDurationInDays() {
        if (startDate != null && endDate != null) {
            long hours = getDurationInHours();
            return (int) Math.ceil(hours / 24.0);
        }
        return 0;
    }
}
