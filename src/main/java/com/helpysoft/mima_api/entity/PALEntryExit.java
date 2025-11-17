package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des entrées et sorties du PAL (Port Autonome de Lomé)
 * Basé sur le cahier des charges - Table 15 Entrée Sortie PAL (Page 13)
 * Enregistrement en deux phases: entrée au PAL puis sortie du PAL
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class PALEntryExit extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Navire de commerce
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips commercialShip;

    /**
     * Date et heure d'entrée au PAL
     */
    @Column(nullable = false)
    private LocalDateTime entryDate;

    /**
     * Motif d'entrée au PAL
     */
    @Column(length = 200)
    private String entryReason;

    /**
     * Zone d'ancrage au PAL
     */
    @Column(length = 100)
    private String anchorageZone;

    /**
     * Numéro d'autorisation d'entrée
     */
    @Column(length = 50)
    private String entryAuthorizationNumber;

    /**
     * Autorité ayant délivré l'autorisation
     */
    @Column(length = 100)
    private String authorizingAuthority;

    /**
     * Date et heure de sortie du PAL
     */
    @Column
    private LocalDateTime exitDate;

    /**
     * Motif de sortie
     */
    @Column(length = 200)
    private String exitReason;

    /**
     * Numéro d'autorisation de sortie
     */
    @Column(length = 50)
    private String exitAuthorizationNumber;

    /**
     * Durée du séjour au PAL (en heures)
     */
    @Column
    private Long stayDurationHours;

    /**
     * Services rendus pendant le séjour au PAL
     */
    @Column(length = 500)
    private String servicesProvided;

    /**
     * Incidents signalés
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
    }

    @PreUpdate
    protected void onUpdate() {
        calculateStayDuration();
    }

    /**
     * Calcule la durée du séjour au PAL en heures
     */
    public void calculateStayDuration() {
        if (entryDate != null && exitDate != null) {
            this.stayDurationHours = java.time.Duration.between(entryDate, exitDate).toHours();
        }
    }

    /**
     * Calcule la durée du séjour en jours
     */
    public Long getStayDurationDays() {
        if (stayDurationHours != null) {
            return stayDurationHours / 24;
        }
        return null;
    }
}
