package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des opérations STS (Ship-to-Ship)
 * Basé sur le cahier des charges - Table 22 STS (Page 14)
 * Transfert de cargaison d'un navire à un autre en mer
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class STSOperation extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro unique de l'opération STS
     */
    @Column(unique = true, length = 50)
    private String operationNumber;

    /**
     * Navire mère (Mother Vessel) - navire donneur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips motherVessel;

    /**
     * Navire receveur (Receiving Vessel)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips receivingVessel;

    /**
     * Date et heure de début de l'opération STS
     */
    @Column(nullable = false)
    private LocalDateTime startDate;

    /**
     * Date et heure de fin de l'opération STS
     */
    @Column
    private LocalDateTime endDate;

    /**
     * Durée de l'opération (en heures)
     */
    @Column
    private Double operationDurationHours;

    /**
     * Type de cargaison transférée
     */
    @Column(nullable = false, length = 200)
    private String cargoType;

    /**
     * Quantité transférée
     */
    @Column(nullable = false)
    private Double quantityTransferred;

    /**
     * Unité de mesure (tonnes, m3, barils, etc.)
     */
    @Column(nullable = false, length = 20)
    private String unit;

    /**
     * Lieu de l'opération STS
     */
    @Column(nullable = false, length = 200)
    private String location;

    /**
     * Latitude du lieu de l'opération
     */
    @Column
    private Double latitude;

    /**
     * Longitude du lieu de l'opération
     */
    @Column
    private Double longitude;

    /**
     * Zone maritime (eaux territoriales, ZEE, haute mer)
     */
    @Column(length = 100)
    private String maritimeZone;

    /**
     * Conditions météorologiques
     */
    @Column(length = 500)
    private String weatherConditions;

    /**
     * État de la mer (échelle de Beaufort)
     */
    @Column
    private Integer seaState;

    /**
     * Compagnie/opérateur STS
     */
    @Column(length = 200)
    private String stsOperator;

    /**
     * Numéro d'autorisation STS
     */
    @Column(length = 100)
    private String authorizationNumber;

    /**
     * Autorité ayant délivré l'autorisation
     */
    @Column(length = 200)
    private String authorizingAuthority;

    /**
     * Moyens navals de supervision
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private NavalVessels supervisingNavalVessel;

    /**
     * Société de surveillance (si applicable)
     */
    @Column(length = 200)
    private String surveyCompany;

    /**
     * Plan d'urgence établi (oui/non)
     */
    @Column
    private Boolean emergencyPlanEstablished = false;

    /**
     * Équipements anti-pollution déployés
     */
    @Column(length = 500)
    private String pollutionPreventionEquipment;

    /**
     * Incidents survenus pendant l'opération
     */
    @Column(length = 1000)
    private String incidents;

    /**
     * Pollution constatée (oui/non)
     */
    @Column
    private Boolean pollutionOccurred = false;

    /**
     * Type de pollution (si applicable)
     */
    @Column(length = 200)
    private String pollutionType;

    /**
     * Mesures prises en cas d'incident
     */
    @Column(length = 1000)
    private String incidentMeasures;

    /**
     * Statut de l'opération
     */
    @Column(nullable = false, length = 50)
    private String status; // PLANIFIEE, EN_COURS, TERMINEE, ANNULEE, SUSPENDUE

    /**
     * Rapport STS établi (oui/non)
     */
    @Column
    private Boolean reportEstablished = false;

    /**
     * Référence du rapport
     */
    @Column(length = 100)
    private String reportReference;

    /**
     * Conformité aux normes internationales (oui/non)
     */
    @Column
    private Boolean compliantWithStandards = true;

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
        if (operationNumber == null) {
            operationNumber = "STS-" + System.currentTimeMillis();
        }
        if (status == null) {
            status = "PLANIFIEE";
        }
        if (emergencyPlanEstablished == null) {
            emergencyPlanEstablished = false;
        }
        if (pollutionOccurred == null) {
            pollutionOccurred = false;
        }
        if (reportEstablished == null) {
            reportEstablished = false;
        }
        if (compliantWithStandards == null) {
            compliantWithStandards = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateOperationDuration();
    }

    /**
     * Calcule la durée de l'opération STS
     */
    public void calculateOperationDuration() {
        if (startDate != null && endDate != null) {
            long minutes = java.time.Duration.between(startDate, endDate).toMinutes();
            this.operationDurationHours = minutes / 60.0;
        }
    }

    /**
     * Vérifie si l'opération est terminée
     */
    public Boolean isCompleted() {
        return "TERMINEE".equals(status) && endDate != null;
    }
}
