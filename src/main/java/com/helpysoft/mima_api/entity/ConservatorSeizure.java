package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des saisies conservatoires des navires
 * Basé sur le cahier des charges - Table 17 Saisie conservatoire (Page 13)
 * Enregistrement en deux phases: saisie puis levée de saisie
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class ConservatorSeizure extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Navire de commerce saisi
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips commercialShip;

    /**
     * Date et heure de la saisie conservatoire
     */
    @Column(nullable = false)
    private LocalDateTime seizureDate;

    /**
     * Autorité ordonnant la saisie (Tribunal, Douanes, etc.)
     */
    @Column(nullable = false, length = 200)
    private String seizingAuthority;

    /**
     * Numéro de l'ordonnance/décision de saisie
     */
    @Column(length = 100)
    private String seizureOrderNumber;

    /**
     * Motif de la saisie
     */
    @Column(nullable = false, length = 500)
    private String seizureReason;

    /**
     * Type de saisie (judiciaire, douanière, maritime, etc.)
     */
    @Column(length = 50)
    private String seizureType;

    /**
     * Montant de la créance (si applicable)
     */
    @Column
    private Double claimAmount;

    /**
     * Lieu de la saisie
     */
    @Column(length = 200)
    private String seizureLocation;

    /**
     * Nom du créancier/plaignant
     */
    @Column(length = 200)
    private String creditorName;

    /**
     * Représentant légal du créancier
     */
    @Column(length = 200)
    private String creditorLegalRepresentative;

    /**
     * Huissier ayant procédé à la saisie
     */
    @Column(length = 200)
    private String bailiffName;

    /**
     * Gardien du navire saisi
     */
    @Column(length = 200)
    private String shipGuardian;

    /**
     * Date et heure de la levée de saisie
     */
    @Column
    private LocalDateTime releaseDate;

    /**
     * Motif de la levée de saisie
     */
    @Column(length = 500)
    private String releaseReason;

    /**
     * Numéro de l'ordonnance/décision de levée
     */
    @Column(length = 100)
    private String releaseOrderNumber;

    /**
     * Montant payé pour la levée (si applicable)
     */
    @Column
    private Double amountPaid;

    /**
     * Durée de la saisie (en heures)
     */
    @Column
    private Long seizureDurationHours;

    /**
     * Statut de la saisie
     */
    @Column(length = 50)
    private String status; // EN_COURS, LEVEE, ANNULEE

    /**
     * Documents relatifs à la saisie
     */
    @Column(length = 500)
    private String relatedDocuments;

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
    }

    @PreUpdate
    protected void onUpdate() {
        calculateSeizureDuration();
        updateStatus();
    }

    /**
     * Calcule la durée de la saisie en heures
     */
    public void calculateSeizureDuration() {
        if (seizureDate != null && releaseDate != null) {
            this.seizureDurationHours = java.time.Duration.between(seizureDate, releaseDate).toHours();
        }
    }

    /**
     * Met à jour le statut en fonction de la levée
     */
    private void updateStatus() {
        if (releaseDate != null && "EN_COURS".equals(status)) {
            this.status = "LEVEE";
        }
    }

    /**
     * Calcule la durée de la saisie en jours
     */
    public Long getSeizureDurationDays() {
        if (seizureDurationHours != null) {
            return seizureDurationHours / 24;
        }
        return null;
    }
}
