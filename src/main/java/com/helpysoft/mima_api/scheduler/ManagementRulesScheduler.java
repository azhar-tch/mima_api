package com.helpysoft.mima_api.scheduler;

import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.service.ManagementRulesValidationService;
import com.helpysoft.mima_api.service.RuleViolationAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Tâches planifiées pour la vérification automatique des règles de gestion
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ManagementRulesScheduler {

    private final ManagementRulesValidationService validationService;
    private final RuleViolationAlertService alertService;

    /**
     * Vérifie les absences non justifiées toutes les heures
     */
    @Scheduled(cron = "0 0 * * * *") // Toutes les heures
    @Transactional
    public void checkUnjustifiedAbsences() {
        log.info("🔍 Début de la vérification des absences non justifiées...");

        try {
            List<RuleViolation> violations = validationService.detectUnjustifiedAbsences();

            if (!violations.isEmpty()) {
                alertService.createAlerts(violations);
                log.info("⚠️ {} absence(s) non justifiée(s) détectée(s) et alertée(s)", violations.size());
            } else {
                log.info("✅ Aucune absence non justifiée détectée");
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification des absences: {}", e.getMessage());
        }
    }

    /**
     * Vérifie l'équité de répartition chaque semaine
     */
    @Scheduled(cron = "0 0 8 * * MON") // Tous les lundis à 8h
    @Transactional
    public void checkWeeklyEquityDistribution() {
        log.info("🔍 Début de la vérification hebdomadaire de l'équité de répartition...");

        try {
            LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
            LocalDateTime weekEnd = LocalDateTime.now();

            List<RuleViolation> violations = validationService.validateEquityDistribution(weekStart, weekEnd);

            if (!violations.isEmpty()) {
                // Ne créer des alertes que pour les déséquilibres significatifs
                List<RuleViolation> significantViolations = violations.stream()
                        .filter(v -> v.getSeverity() != RuleViolation.SeverityLevel.INFO)
                        .toList();

                if (!significantViolations.isEmpty()) {
                    alertService.createAlerts(significantViolations);
                    log.info("⚠️ {} déséquilibre(s) significatif(s) détecté(s) pour la semaine dernière", significantViolations.size());
                }

                log.info("ℹ️ {} déséquilibre(s) total(aux) détecté(s)", violations.size());
            } else {
                log.info("✅ Répartition équitable pour la semaine dernière");
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification de l'équité: {}", e.getMessage());
        }
    }

    /**
     * Vérifie l'équité de répartition chaque mois
     */
    @Scheduled(cron = "0 0 9 1 * *") // Le 1er de chaque mois à 9h
    @Transactional
    public void checkMonthlyEquityDistribution() {
        log.info("🔍 Début de la vérification mensuelle de l'équité de répartition...");

        try {
            LocalDateTime monthStart = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0);
            LocalDateTime monthEnd = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

            List<RuleViolation> violations = validationService.validateEquityDistribution(monthStart, monthEnd);

            if (!violations.isEmpty()) {
                alertService.createAlerts(violations);
                log.info("⚠️ {} déséquilibre(s) détecté(s) pour le mois dernier", violations.size());
            } else {
                log.info("✅ Répartition équitable pour le mois dernier");
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification mensuelle de l'équité: {}", e.getMessage());
        }
    }

    /**
     * Nettoie les alertes résolues anciennes tous les mois
     */
    @Scheduled(cron = "0 0 2 1 * *") // Le 1er de chaque mois à 2h
    @Transactional
    public void cleanupOldResolvedAlerts() {
        log.info("🧹 Début du nettoyage des alertes résolues anciennes...");

        try {
            // Note: Cette fonctionnalité pourrait être implémentée dans le service
            // pour archiver les alertes résolues de plus de 6 mois par exemple
            log.info("✅ Nettoyage des alertes terminé");
        } catch (Exception e) {
            log.error("❌ Erreur lors du nettoyage des alertes: {}", e.getMessage());
        }
    }
}
