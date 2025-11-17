package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité pour la gestion des primes du personnel
 * Basé sur le cahier des charges - Table Prime personnel
 * Montant journalier obtenu par le personnel militaire en fonction de leur grade
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "personnel_allowances")
public class PersonnelAllowance extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Code du grade (lié à MaritimeRank)
     * Exemples: "CC", "SM", "MT1", "MT2", "PM", "SM", "QM1", "QM2", "MLT1", "MLT2"
     */
    @Column(name = "rank_code", nullable = false, unique = true, length = 20)
    private String rankCode;

    /**
     * Grade maritime correspondant
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "maritime_rank", nullable = false)
    private MaritimeRank maritimeRank;

    /**
     * Montant de la prime journalière pour les missions d'escorte
     */
    @Column(name = "escort_daily_allowance", nullable = false, precision = 10, scale = 2)
    private BigDecimal escortDailyAllowance;

    /**
     * Montant de la prime journalière pour les missions de garde armée
     */
    @Column(name = "armed_guard_daily_allowance", nullable = false, precision = 10, scale = 2)
    private BigDecimal armedGuardDailyAllowance;

    /**
     * Prime de patrouille (pour missions de patrouille)
     */
    @Column(name = "patrol_allowance", precision = 10, scale = 2)
    private BigDecimal patrolAllowance;

    /**
     * Prime de risque
     */
    @Column(name = "risk_allowance", precision = 10, scale = 2)
    private BigDecimal riskAllowance;

    /**
     * Prime de mer
     */
    @Column(name = "sea_allowance", precision = 10, scale = 2)
    private BigDecimal seaAllowance;

    /**
     * Devise (généralement XOF - Franc CFA)
     */
    @Column(name = "currency", length = 3)
    private String currency = "XOF";

    @Column(name = "observations", length = 500)
    private String observations;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
        if (currency == null) {
            currency = "XOF";
        }
    }

    /**
     * Calcule la prime totale pour un nombre de jours d'escorte
     */
    public BigDecimal calculateEscortAllowance(int days) {
        return escortDailyAllowance.multiply(BigDecimal.valueOf(days));
    }

    /**
     * Calcule la prime totale pour un nombre de jours de garde armée
     */
    public BigDecimal calculateArmedGuardAllowance(int days) {
        return armedGuardDailyAllowance.multiply(BigDecimal.valueOf(days));
    }
}
