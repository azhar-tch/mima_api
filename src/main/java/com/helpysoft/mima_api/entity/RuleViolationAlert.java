package com.helpysoft.mima_api.entity;

import com.helpysoft.mima_api.dto.RuleViolation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour stocker les alertes de violation de règles de gestion
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rule_violation_alerts")
public class RuleViolationAlert extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Type de règle violée
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleViolation.RuleType ruleType;

    /**
     * Niveau de sévérité
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleViolation.SeverityLevel severity;

    /**
     * Message descriptif
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * Agent concerné
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agents agent;

    /**
     * Mission concernée (optionnel)
     */
    @Column
    private UUID missionTrackingId;

    /**
     * Détails supplémentaires
     */
    @Column(columnDefinition = "TEXT")
    private String details;

    /**
     * Statut de l'alerte
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.ACTIVE;

    /**
     * L'alerte peut-elle être annulée par un administrateur
     */
    @Column(nullable = false)
    private Boolean canBeOverridden = false;

    /**
     * Date de résolution
     */
    @Column
    private LocalDateTime resolvedDate;

    /**
     * Utilisateur qui a résolu l'alerte
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_id")
    private Users resolvedBy;

    /**
     * Commentaire de résolution
     */
    @Column(columnDefinition = "TEXT")
    private String resolutionComment;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }

    public enum AlertStatus {
        ACTIVE,      // Alerte active
        RESOLVED,    // Alerte résolue
        OVERRIDDEN,  // Alerte annulée par un admin
        DISMISSED    // Alerte rejetée
    }
}
