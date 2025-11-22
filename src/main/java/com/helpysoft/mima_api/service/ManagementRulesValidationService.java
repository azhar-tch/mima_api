package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.Agents;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service de validation des règles de gestion
 * Implémente les 6 règles principales du cahier des charges
 */
public interface ManagementRulesValidationService {

    /**
     * Règle 1: Vérifie qu'il n'y a pas de double affectation simultanée
     * @param agentTrackingId Agent à vérifier
     * @param startDate Date de début de l'affectation
     * @param endDate Date de fin de l'affectation
     * @param currentMissionTrackingId Mission actuelle (pour exclure lors des mises à jour)
     * @return Liste des violations détectées
     */
    List<RuleViolation> validateNoDoubleAssignment(
            UUID agentTrackingId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            UUID currentMissionTrackingId
    );

    /**
     * Règle 2: Vérifie le respect du repos minimal entre missions
     * @param agentTrackingId Agent à vérifier
     * @param newMissionStartDate Date de début de la nouvelle mission
     * @return Liste des violations détectées
     */
    List<RuleViolation> validateMinimumRest(
            UUID agentTrackingId,
            LocalDateTime newMissionStartDate
    );

    /**
     * Règle 3: Vérifie que la durée hebdomadaire maximale n'est pas dépassée
     * @param agentTrackingId Agent à vérifier
     * @param weekStartDate Début de la semaine
     * @param additionalHours Heures supplémentaires à ajouter
     * @return Liste des violations détectées
     */
    List<RuleViolation> validateWeeklyMaxHours(
            UUID agentTrackingId,
            LocalDateTime weekStartDate,
            Long additionalHours
    );

    /**
     * Règle 4: Vérifie et signale les absences non justifiées
     * @return Liste des absences non justifiées détectées
     */
    List<RuleViolation> detectUnjustifiedAbsences();

    /**
     * Règle 5: Vérifie l'équité de répartition des missions
     * @param periodStart Début de la période
     * @param periodEnd Fin de la période
     * @return Liste des violations d'équité détectées
     */
    List<RuleViolation> validateEquityDistribution(
            LocalDateTime periodStart,
            LocalDateTime periodEnd
    );

    /**
     * Règle 6: Valide toutes les règles pour une affectation
     * @param agentTrackingId Agent concerné
     * @param startDate Date de début
     * @param endDate Date de fin
     * @param currentMissionTrackingId Mission actuelle (null pour création)
     * @return Liste complète des violations
     */
    List<RuleViolation> validateAllRules(
            UUID agentTrackingId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            UUID currentMissionTrackingId
    );

    /**
     * Calcule les heures travaillées par un agent sur une période
     */
    Long calculateWorkedHours(UUID agentTrackingId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Récupère les règles de gestion actives
     */
    com.helpysoft.mima_api.entity.ManagementRules getActiveManagementRules();
}
