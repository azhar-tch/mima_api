package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion du personnel participant aux missions de garde armée
 * Basé sur le cahier des charges - Table Mission Personnel
 * Enregistrement en deux phases: embarquement puis débarquement
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "armed_guard_personnel")
public class ArmedGuardPersonnel extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Mission de garde armée
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "armed_guard_mission_id", nullable = false)
    private ArmedGuardMission armedGuardMission;

    /**
     * Membre du personnel
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agents agent;

    /**
     * Grade au moment de la mission
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    private MaritimeRank rank;

    /**
     * Fonction pendant la mission
     */
    @Column(name = "function", length = 100)
    private String function;

    /**
     * Date et heure d'embarquement du personnel
     */
    @Column(name = "embarkation_date", nullable = false)
    private LocalDateTime embarkationDate;

    /**
     * Date et heure de débarquement du personnel
     */
    @Column(name = "disembarkation_date")
    private LocalDateTime disembarkationDate;

    /**
     * Nombre de jours effectués (calculé automatiquement)
     */
    @Column(name = "days_count")
    private Integer daysCount;

    /**
     * Prime calculée pour cette mission (en fonction du grade et de la durée)
     */
    @Column(name = "calculated_allowance", precision = 10, scale = 2)
    private BigDecimal calculatedAllowance;

    /**
     * Observations
     */
    @Column(name = "observations", length = 500)
    private String observations;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateDaysCount();
    }

    /**
     * Calcule le nombre de jours
     * Si embarquement et débarquement le même jour, compte 1 jour
     */
    public void calculateDaysCount() {
        if (embarkationDate != null && disembarkationDate != null) {
            long days = java.time.Duration.between(embarkationDate, disembarkationDate).toDays();
            // Si même jour, compter au moins 1 jour selon le cahier des charges
            this.daysCount = days == 0 ? 1 : (int) days;
        }
    }
}
