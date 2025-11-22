package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.RuleViolationAlert;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.RuleViolationAlertRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.RuleViolationAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RuleViolationAlertServiceImpl implements RuleViolationAlertService {

    private final RuleViolationAlertRepository alertRepository;
    private final AgentsRepository agentsRepository;
    private final UsersRepository usersRepository;
    private final NotificationsServiceImpl notificationsService;

    @Override
    public RuleViolationAlert createAlert(RuleViolation violation) {
        Agents agent = agentsRepository.findByTrackingId(violation.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        RuleViolationAlert alert = new RuleViolationAlert();
        alert.setRuleType(violation.getRuleType());
        alert.setSeverity(violation.getSeverity());
        alert.setMessage(violation.getMessage());
        alert.setAgent(agent);
        alert.setMissionTrackingId(violation.getMissionTrackingId());
        alert.setDetails(violation.getDetails());
        alert.setCanBeOverridden(violation.getCanBeOverridden());
        alert.setStatus(RuleViolationAlert.AlertStatus.ACTIVE);

        RuleViolationAlert savedAlert = alertRepository.save(alert);

        // Envoyer une notification aux administrateurs
        notifyAdminsOfAlert(savedAlert);

        log.info("✅ Alerte de violation créée: {} - Agent: {}",
                alert.getRuleType(), agent.getFirstName() + " " + agent.getLastName());

        return savedAlert;
    }

    @Override
    public List<RuleViolationAlert> createAlerts(List<RuleViolation> violations) {
        List<RuleViolationAlert> alerts = new ArrayList<>();

        for (RuleViolation violation : violations) {
            try {
                RuleViolationAlert alert = createAlert(violation);
                alerts.add(alert);
            } catch (Exception e) {
                log.error("❌ Erreur lors de la création de l'alerte: {}", e.getMessage());
            }
        }

        return alerts;
    }

    @Override
    @Transactional(readOnly = true)
    public RuleViolationAlert findByTrackingId(UUID trackingId) {
        return alertRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleViolationAlert> findActiveAlerts() {
        return alertRepository.findByStatus(RuleViolationAlert.AlertStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleViolationAlert> findByAgent(UUID agentTrackingId) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        return alertRepository.findByAgent(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleViolationAlert> findByStatus(RuleViolationAlert.AlertStatus status) {
        return alertRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleViolationAlert> findByRuleType(RuleViolation.RuleType ruleType) {
        return alertRepository.findByRuleType(ruleType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleViolationAlert> findCriticalActiveAlerts() {
        return alertRepository.findBySeverityAndStatus(
                RuleViolation.SeverityLevel.CRITICAL,
                RuleViolationAlert.AlertStatus.ACTIVE
        );
    }

    @Override
    public RuleViolationAlert resolveAlert(UUID trackingId, UUID resolvedByUserId, String comment) {
        RuleViolationAlert alert = findByTrackingId(trackingId);
        Users user = usersRepository.findByTrackingId(resolvedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        alert.setStatus(RuleViolationAlert.AlertStatus.RESOLVED);
        alert.setResolvedDate(LocalDateTime.now());
        alert.setResolvedBy(user);
        alert.setResolutionComment(comment);

        RuleViolationAlert savedAlert = alertRepository.save(alert);

        log.info("✅ Alerte résolue: {} - Par: {}", trackingId, user.getUsername());

        return savedAlert;
    }

    @Override
    public RuleViolationAlert overrideAlert(UUID trackingId, UUID overriddenByUserId, String comment) {
        RuleViolationAlert alert = findByTrackingId(trackingId);

        if (!alert.getCanBeOverridden()) {
            throw new RuntimeException("Cette alerte ne peut pas être annulée");
        }

        Users user = usersRepository.findByTrackingId(overriddenByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        alert.setStatus(RuleViolationAlert.AlertStatus.OVERRIDDEN);
        alert.setResolvedDate(LocalDateTime.now());
        alert.setResolvedBy(user);
        alert.setResolutionComment(comment);

        RuleViolationAlert savedAlert = alertRepository.save(alert);

        log.info("✅ Alerte annulée: {} - Par: {}", trackingId, user.getUsername());

        return savedAlert;
    }

    @Override
    public RuleViolationAlert dismissAlert(UUID trackingId, UUID dismissedByUserId, String comment) {
        RuleViolationAlert alert = findByTrackingId(trackingId);
        Users user = usersRepository.findByTrackingId(dismissedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        alert.setStatus(RuleViolationAlert.AlertStatus.DISMISSED);
        alert.setResolvedDate(LocalDateTime.now());
        alert.setResolvedBy(user);
        alert.setResolutionComment(comment);

        RuleViolationAlert savedAlert = alertRepository.save(alert);

        log.info("✅ Alerte rejetée: {} - Par: {}", trackingId, user.getUsername());

        return savedAlert;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveAlerts() {
        return alertRepository.countByStatus(RuleViolationAlert.AlertStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countCriticalActiveAlerts() {
        return (long) findCriticalActiveAlerts().size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleViolationAlert> findByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return alertRepository.findByPeriod(startDate, endDate);
    }

    /**
     * Notifie tous les administrateurs d'une nouvelle alerte
     */
    private void notifyAdminsOfAlert(RuleViolationAlert alert) {
        // Récupérer tous les utilisateurs (dans un système réel, filtrer par rôle ADMIN)
        List<Users> allUsers = usersRepository.findAll();

        String severityIcon = getSeverityIcon(alert.getSeverity());
        String message = String.format(
                "%s Alerte de violation de règle - %s: %s",
                severityIcon,
                alert.getRuleType(),
                alert.getMessage()
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("rule_violation");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification d'alerte: {}", e.getMessage());
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
}
