package com.helpysoft.mima_api.scheduler;

import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.ManagementRulesValidationService;
import com.helpysoft.mima_api.serviceImpl.NotificationsServiceImpl;
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
    private final NotificationsServiceImpl notificationsService;
    private final UsersRepository usersRepository;

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
                sendViolationNotifications(violations, "absences_non_justifiees");
                log.info("⚠️ {} absence(s) non justifiée(s) détectée(s) et notifiée(s)", violations.size());
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
                // Ne notifier que pour les déséquilibres significatifs
                List<RuleViolation> significantViolations = violations.stream()
                        .filter(v -> v.getSeverity() != RuleViolation.SeverityLevel.INFO)
                        .toList();

                if (!significantViolations.isEmpty()) {
                    sendViolationNotifications(significantViolations, "equite_repartition_hebdo");
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
                sendViolationNotifications(violations, "equite_repartition_mensuel");
                log.info("⚠️ {} déséquilibre(s) détecté(s) pour le mois dernier", violations.size());
            } else {
                log.info("✅ Répartition équitable pour le mois dernier");
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de la vérification mensuelle de l'équité: {}", e.getMessage());
        }
    }

    /**
     * Envoie des notifications pour les violations de règles à tous les utilisateurs
     */
    private void sendViolationNotifications(List<RuleViolation> violations, String notificationType) {
        List<Users> allUsers = usersRepository.findAll();

        for (RuleViolation violation : violations) {
            String severityIcon = getSeverityIcon(violation.getSeverity());
            String message = String.format("%s %s: %s",
                severityIcon,
                getRuleTypeLabel(violation.getRuleType()),
                violation.getMessage()
            );

            for (Users user : allUsers) {
                try {
                    NotificationsRequest notificationRequest = new NotificationsRequest();
                    notificationRequest.setMessage(message);
                    notificationRequest.setNotificationType(notificationType);
                    notificationRequest.setRecipientTrackingId(user.getTrackingId());

                    notificationsService.create(notificationRequest);
                } catch (Exception e) {
                    log.error("❌ Erreur lors de l'envoi de la notification de violation: {}", e.getMessage());
                }
            }
        }
    }

    private String getSeverityIcon(RuleViolation.SeverityLevel severity) {
        switch (severity) {
            case CRITICAL:
                return "🔴";
            case ERROR:
                return "🟠";
            case WARNING:
                return "🟡";
            case INFO:
                return "ℹ️";
            default:
                return "⚠️";
        }
    }

    private String getRuleTypeLabel(RuleViolation.RuleType ruleType) {
        switch (ruleType) {
            case DOUBLE_ASSIGNMENT:
                return "Double affectation";
            case INSUFFICIENT_REST:
                return "Repos insuffisant";
            case WEEKLY_HOURS_EXCEEDED:
                return "Durée hebdomadaire dépassée";
            case UNJUSTIFIED_ABSENCE:
                return "Absence non justifiée";
            case EQUITY_DISTRIBUTION:
                return "Déséquilibre de répartition";
            default:
                return "Violation de règle";
        }
    }
}
