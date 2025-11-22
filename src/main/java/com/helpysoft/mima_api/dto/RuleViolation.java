package com.helpysoft.mima_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente une violation de règle de gestion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleViolation {
    /**
     * Type de règle violée
     */
    private RuleType ruleType;

    /**
     * Niveau de sévérité (WARNING, ERROR, CRITICAL)
     */
    private SeverityLevel severity;

    /**
     * Message descriptif de la violation
     */
    private String message;

    /**
     * Agent concerné par la violation
     */
    private UUID agentTrackingId;

    /**
     * Nom de l'agent
     */
    private String agentName;

    /**
     * Mission/affectation concernée (optionnel)
     */
    private UUID missionTrackingId;

    /**
     * Date de détection de la violation
     */
    private LocalDateTime detectionDate;

    /**
     * Détails supplémentaires
     */
    private String details;

    /**
     * Peut être ignorée par un utilisateur autorisé
     */
    private Boolean canBeOverridden;

    public enum RuleType {
        DOUBLE_ASSIGNMENT,      // Règle 1: Double affectation
        INSUFFICIENT_REST,      // Règle 2: Repos minimal
        WEEKLY_HOURS_EXCEEDED,  // Règle 3: Durée hebdomadaire
        UNJUSTIFIED_ABSENCE,    // Règle 4: Absence non justifiée
        EQUITY_DISTRIBUTION,    // Règle 5: Équité de répartition
        GENERAL_VIOLATION       // Règle 6: Violation générale
    }

    public enum SeverityLevel {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
}
