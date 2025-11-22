package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.RuleViolationAlert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service de gestion des alertes de violation de règles
 */
public interface RuleViolationAlertService {

    /**
     * Crée une alerte à partir d'une violation
     */
    RuleViolationAlert createAlert(RuleViolation violation);

    /**
     * Crée plusieurs alertes à partir d'une liste de violations
     */
    List<RuleViolationAlert> createAlerts(List<RuleViolation> violations);

    /**
     * Récupère une alerte par son trackingId
     */
    RuleViolationAlert findByTrackingId(UUID trackingId);

    /**
     * Récupère toutes les alertes actives
     */
    List<RuleViolationAlert> findActiveAlerts();

    /**
     * Récupère les alertes par agent
     */
    List<RuleViolationAlert> findByAgent(UUID agentTrackingId);

    /**
     * Récupère les alertes par statut
     */
    List<RuleViolationAlert> findByStatus(RuleViolationAlert.AlertStatus status);

    /**
     * Récupère les alertes par type de règle
     */
    List<RuleViolationAlert> findByRuleType(RuleViolation.RuleType ruleType);

    /**
     * Récupère les alertes critiques actives
     */
    List<RuleViolationAlert> findCriticalActiveAlerts();

    /**
     * Résout une alerte
     */
    RuleViolationAlert resolveAlert(UUID trackingId, UUID resolvedByUserId, String comment);

    /**
     * Annule une alerte (override)
     */
    RuleViolationAlert overrideAlert(UUID trackingId, UUID overriddenByUserId, String comment);

    /**
     * Rejette une alerte
     */
    RuleViolationAlert dismissAlert(UUID trackingId, UUID dismissedByUserId, String comment);

    /**
     * Compte les alertes actives
     */
    Long countActiveAlerts();

    /**
     * Compte les alertes critiques actives
     */
    Long countCriticalActiveAlerts();

    /**
     * Récupère les alertes par période
     */
    List<RuleViolationAlert> findByPeriod(LocalDateTime startDate, LocalDateTime endDate);
}
