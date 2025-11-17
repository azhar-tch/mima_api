package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des avitaillements des navires de commerce
 * Basé sur le cahier des charges - Table 19 Avitaillement (Page 13-14)
 * Inclut également les retards des avitailleurs (Table 21)
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class ShipProvisioning extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Navire de commerce avitaillé
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips commercialShip;

    /**
     * Date et heure de l'avitaillement
     */
    @Column(nullable = false)
    private LocalDateTime provisioningDate;

    /**
     * Type d'avitaillement (carburant, vivres, eau, matériel, etc.)
     */
    @Column(nullable = false, length = 100)
    private String provisioningType;

    /**
     * Fournisseur/Avitailleur
     */
    @Column(length = 200)
    private String supplierName;

    /**
     * Bateau/embarcation de servitude utilisé
     */
    @Column(length = 100)
    private String supplyVesselName;

    /**
     * Numéro IMO du bateau de servitude (si applicable)
     */
    @Column(length = 10)
    private String supplyVesselImo;

    /**
     * Type de produit avitaillé
     */
    @Column(length = 200)
    private String productType;

    /**
     * Quantité avitaillée
     */
    @Column
    private Double quantity;

    /**
     * Unité de mesure (litres, tonnes, m3, etc.)
     */
    @Column(length = 20)
    private String unit;

    /**
     * Montant de la prestation
     */
    @Column
    private Double amount;

    /**
     * Heure de début de l'opération
     */
    @Column
    private LocalDateTime startTime;

    /**
     * Heure de fin de l'opération
     */
    @Column
    private LocalDateTime endTime;

    /**
     * Durée de l'opération (en heures)
     */
    @Column
    private Double operationDurationHours;

    /**
     * Point d'avitaillement (quai, mouillage, en mer, etc.)
     */
    @Column(length = 100)
    private String provisioningPoint;

    /**
     * RETARD AVITAILLEUR (Table 21 intégrée)
     * Retard constaté (oui/non)
     */
    @Column
    private Boolean hasDelay = false;

    /**
     * Durée du retard (en heures)
     */
    @Column
    private Double delayDurationHours;

    /**
     * Motif du retard
     */
    @Column(length = 500)
    private String delayReason;

    /**
     * Pénalités appliquées pour retard
     */
    @Column
    private Double delayPenalty;

    /**
     * Mesures correctives prises
     */
    @Column(length = 500)
    private String correctiveActions;

    /**
     * Incidents pendant l'avitaillement
     */
    @Column(length = 1000)
    private String incidents;

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
        if (hasDelay == null) {
            hasDelay = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateOperationDuration();
    }

    /**
     * Calcule la durée de l'opération d'avitaillement
     */
    public void calculateOperationDuration() {
        if (startTime != null && endTime != null) {
            long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
            this.operationDurationHours = minutes / 60.0;
        }
    }

    /**
     * Vérifie si l'avitaillement a été effectué dans les délais
     */
    public Boolean isOnTime() {
        return !hasDelay;
    }
}
