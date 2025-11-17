package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des incidents et assistances concernant les navires
 * Basé sur le cahier des charges - Table 20 Incident/Assistance (Page 14)
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class ShipIncident extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Navire de commerce concerné
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips commercialShip;

    /**
     * Date et heure de l'incident
     */
    @Column(nullable = false)
    private LocalDateTime incidentDate;

    /**
     * Type d'événement (INCIDENT ou ASSISTANCE)
     */
    @Column(nullable = false, length = 20)
    private String eventType; // INCIDENT, ASSISTANCE

    /**
     * Type d'incident/assistance
     */
    @Column(nullable = false, length = 100)
    private String incidentType;

    /**
     * Gravité de l'incident (FAIBLE, MOYENNE, GRAVE, CRITIQUE)
     */
    @Column(length = 20)
    private String severity;

    /**
     * Lieu de l'incident (coordonnées GPS ou description)
     */
    @Column(nullable = false, length = 200)
    private String location;

    /**
     * Latitude
     */
    @Column
    private Double latitude;

    /**
     * Longitude
     */
    @Column
    private Double longitude;

    /**
     * Zone maritime (eaux territoriales, ZEE, haute mer, port, etc.)
     */
    @Column(length = 100)
    private String maritimeZone;

    /**
     * Description détaillée de l'incident
     */
    @Column(nullable = false, length = 2000)
    private String description;

    /**
     * Causes de l'incident
     */
    @Column(length = 1000)
    private String causes;

    /**
     * Victimes (nombre de blessés, décès)
     */
    @Column(length = 500)
    private String casualties;

    /**
     * Dégâts matériels
     */
    @Column(length = 1000)
    private String materialDamage;

    /**
     * Pollution constatée (oui/non)
     */
    @Column
    private Boolean pollutionOccurred = false;

    /**
     * Type de pollution (hydrocarbure, chimique, etc.)
     */
    @Column(length = 200)
    private String pollutionType;

    /**
     * Intervenants (Marine, Pompiers, CROSS, etc.)
     */
    @Column(length = 500)
    private String respondingAgencies;

    /**
     * Moyens navals déployés pour assistance
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private NavalVessels assistingNavalVessel;

    /**
     * Mesures prises immédiatement
     */
    @Column(nullable = false, length = 2000)
    private String immediateMeasures;

    /**
     * Date de résolution de l'incident
     */
    @Column
    private LocalDateTime resolutionDate;

    /**
     * Statut de l'incident
     */
    @Column(nullable = false, length = 50)
    private String status; // EN_COURS, RESOLU, EN_INVESTIGATION

    /**
     * Rapport établi (oui/non)
     */
    @Column
    private Boolean reportEstablished = false;

    /**
     * Référence du rapport
     */
    @Column(length = 100)
    private String reportReference;

    /**
     * Autorités notifiées (Préfecture Maritime, etc.)
     */
    @Column(length = 500)
    private String notifiedAuthorities;

    /**
     * Enquête en cours (oui/non)
     */
    @Column
    private Boolean investigationOngoing = false;

    /**
     * Recommandations pour éviter récidive
     */
    @Column(length = 1000)
    private String recommendations;

    /**
     * Observations
     */
    @Column(length = 1000)
    private String observations;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
        if (status == null) {
            status = "EN_COURS";
        }
        if (pollutionOccurred == null) {
            pollutionOccurred = false;
        }
        if (reportEstablished == null) {
            reportEstablished = false;
        }
        if (investigationOngoing == null) {
            investigationOngoing = false;
        }
    }

    /**
     * Vérifie si l'incident est résolu
     */
    public Boolean isResolved() {
        return "RESOLU".equals(status) && resolutionDate != null;
    }

    /**
     * Calcule la durée de résolution en heures
     */
    public Long getResolutionDurationHours() {
        if (incidentDate != null && resolutionDate != null) {
            return java.time.Duration.between(incidentDate, resolutionDate).toHours();
        }
        return null;
    }
}
