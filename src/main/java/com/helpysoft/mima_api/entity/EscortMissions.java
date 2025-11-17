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
public class EscortMissions extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro d'identification automatique de la mission d'escorte
     */
    @Column(nullable = false, unique = true, length = 50)
    private String missionNumber;

    /**
     * Navire escorté
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips commercialShip;

    /**
     * Agence de sécurité qui a effectué la demande d'escorte
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SecurityAgencies securityAgency;

    /**
     * Patrouilleur escorteur principal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private NavalVessels navalVessel;

    /**
     * Commandant du patrouilleur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Agents commander;

    /**
     * Grade du commandant au moment de la mission
     */
    @Column(length = 50)
    private String commanderRank;

    /**
     * Autre navire escorte (optionnel - pour escortes simultanées)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private NavalVessels secondaryVessel;

    /**
     * Vedettes (optionnel - pour missions avec vedettes)
     */
    @Column(length = 200)
    private String vedettes; // Liste de vedettes séparées par virgules

    /**
     * Date et heure de début de mission
     */
    @Column(nullable = false)
    private LocalDateTime startDate;

    /**
     * Date et heure de fin de mission
     */
    @Column
    private LocalDateTime endDate;

    /**
     * Type d'escorte
     */
    @Enumerated(EnumType.STRING)
    @Column
    private EscortType escortType;

    /**
     * Point de départ
     */
    @Column(length = 200)
    private String departurePoint;

    /**
     * Point d'arrivée
     */
    @Column(length = 200)
    private String arrivalPoint;

    /**
     * Distance parcourue en miles nautiques
     */
    @Column
    private Double distance;

    /**
     * Zone d'escorte
     */
    @Column(length = 200)
    private String escortZone;

    /**
     * Statut de la mission
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status = MissionStatus.IN_PROGRESS;

    /**
     * Incidents rencontrés
     */
    @Column(length = 1000)
    private String incidents;

    /**
     * Observations
     */
    @Column(length = 1000)
    private String observations;

    /**
     * Personnel de l'équipage
     */
    @OneToMany(mappedBy = "escortMission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EscortPersonnels> crew = new ArrayList<>();

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
