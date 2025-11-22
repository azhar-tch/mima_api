package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.*;
import com.helpysoft.mima_api.repository.*;
import com.helpysoft.mima_api.service.ManagementRulesValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de validation des règles de gestion
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ManagementRulesValidationServiceImpl implements ManagementRulesValidationService {

    private final ManagementRulesRepository managementRulesRepository;
    private final ArmedGuardPersonnelRepository armedGuardPersonnelRepository;
    private final EscortPersonnelRepository escortPersonnelRepository;
    private final AbsencesRepository absencesRepository;
    private final AgentsRepository agentsRepository;

    @Override
    public List<RuleViolation> validateNoDoubleAssignment(
            UUID agentTrackingId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            UUID currentMissionTrackingId
    ) {
        List<RuleViolation> violations = new ArrayList<>();
        ManagementRules rules = getActiveManagementRules();

        // Si la règle n'est pas activée, retourner vide
        if (rules == null || !rules.getPreventDoubleAssignment()) {
            return violations;
        }

        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Vérifier les affectations dans ArmedGuard
        List<ArmedGuardPersonnels> armedGuardAssignments = armedGuardPersonnelRepository.findByAgentId(agent.getId());
        for (ArmedGuardPersonnels assignment : armedGuardAssignments) {
            // Ignorer la mission actuelle lors des mises à jour
            if (currentMissionTrackingId != null &&
                assignment.getArmedGuardMission().getTrackingId().equals(currentMissionTrackingId)) {
                continue;
            }

            LocalDateTime missionStart = assignment.getEmbarkationDate();
            LocalDateTime missionEnd = assignment.getDisembarkationDate();

            if (missionEnd != null && periodsOverlap(startDate, endDate, missionStart, missionEnd)) {
                RuleViolation violation = new RuleViolation();
                violation.setRuleType(RuleViolation.RuleType.DOUBLE_ASSIGNMENT);
                violation.setSeverity(RuleViolation.SeverityLevel.CRITICAL);
                violation.setAgentTrackingId(agentTrackingId);
                violation.setAgentName(agent.getFirstName() + " " + agent.getLastName());
                violation.setMissionTrackingId(assignment.getArmedGuardMission().getTrackingId());
                violation.setDetectionDate(LocalDateTime.now());
                violation.setMessage(String.format(
                    "Double affectation détectée: %s est déjà affecté(e) à la mission %s du %s au %s",
                    violation.getAgentName(),
                    assignment.getArmedGuardMission().getMissionNumber(),
                    missionStart,
                    missionEnd
                ));
                violation.setCanBeOverridden(false);
                violations.add(violation);
            }
        }

        // Vérifier les affectations dans Escort
        List<EscortPersonnels> escortAssignments = escortPersonnelRepository.findRealMissionsByAgentId(agent.getId());
        for (EscortPersonnels assignment : escortAssignments) {
            // Ignorer la mission actuelle lors des mises à jour
            if (currentMissionTrackingId != null &&
                assignment.getEscortMission().getTrackingId().equals(currentMissionTrackingId)) {
                continue;
            }

            LocalDateTime missionStart = assignment.getEscortMission().getStartDate();
            LocalDateTime missionEnd = assignment.getEscortMission().getEndDate();

            if (missionEnd != null && periodsOverlap(startDate, endDate, missionStart, missionEnd)) {
                RuleViolation violation = new RuleViolation();
                violation.setRuleType(RuleViolation.RuleType.DOUBLE_ASSIGNMENT);
                violation.setSeverity(RuleViolation.SeverityLevel.CRITICAL);
                violation.setAgentTrackingId(agentTrackingId);
                violation.setAgentName(agent.getFirstName() + " " + agent.getLastName());
                violation.setMissionTrackingId(assignment.getEscortMission().getTrackingId());
                violation.setDetectionDate(LocalDateTime.now());
                violation.setMessage(String.format(
                    "Double affectation détectée: %s est déjà affecté(e) à la mission d'escorte %s du %s au %s",
                    violation.getAgentName(),
                    assignment.getEscortMission().getMissionNumber(),
                    missionStart,
                    missionEnd
                ));
                violation.setCanBeOverridden(false);
                violations.add(violation);
            }
        }

        return violations;
    }

    @Override
    public List<RuleViolation> validateMinimumRest(
            UUID agentTrackingId,
            LocalDateTime newMissionStartDate
    ) {
        List<RuleViolation> violations = new ArrayList<>();
        ManagementRules rules = getActiveManagementRules();

        if (rules == null || rules.getMinRestHours() == 0) {
            return violations;
        }

        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        LocalDateTime minimumRestEndTime = newMissionStartDate.minusHours(rules.getMinRestHours());

        // Vérifier la dernière mission ArmedGuard
        List<ArmedGuardPersonnels> armedGuardAssignments = armedGuardPersonnelRepository.findByAgentId(agent.getId());
        for (ArmedGuardPersonnels assignment : armedGuardAssignments) {
            if (assignment.getDisembarkationDate() != null &&
                assignment.getDisembarkationDate().isAfter(minimumRestEndTime) &&
                assignment.getDisembarkationDate().isBefore(newMissionStartDate)) {

                long restHours = Duration.between(assignment.getDisembarkationDate(), newMissionStartDate).toHours();

                RuleViolation violation = new RuleViolation();
                violation.setRuleType(RuleViolation.RuleType.INSUFFICIENT_REST);
                violation.setSeverity(RuleViolation.SeverityLevel.ERROR);
                violation.setAgentTrackingId(agentTrackingId);
                violation.setAgentName(agent.getFirstName() + " " + agent.getLastName());
                violation.setDetectionDate(LocalDateTime.now());
                violation.setMessage(String.format(
                    "Repos insuffisant: %s a terminé la mission %s le %s. " +
                    "Repos de %d heures < minimum requis de %d heures",
                    violation.getAgentName(),
                    assignment.getArmedGuardMission().getMissionNumber(),
                    assignment.getDisembarkationDate(),
                    restHours,
                    rules.getMinRestHours()
                ));
                violation.setDetails(String.format("Dernière mission terminée le %s", assignment.getDisembarkationDate()));
                violation.setCanBeOverridden(true);
                violations.add(violation);
            }
        }

        // Vérifier la dernière mission Escort
        List<EscortPersonnels> escortAssignments = escortPersonnelRepository.findRealMissionsByAgentId(agent.getId());
        for (EscortPersonnels assignment : escortAssignments) {
            LocalDateTime escortEnd = assignment.getEscortMission().getEndDate();
            if (escortEnd != null &&
                escortEnd.isAfter(minimumRestEndTime) &&
                escortEnd.isBefore(newMissionStartDate)) {

                long restHours = Duration.between(escortEnd, newMissionStartDate).toHours();

                RuleViolation violation = new RuleViolation();
                violation.setRuleType(RuleViolation.RuleType.INSUFFICIENT_REST);
                violation.setSeverity(RuleViolation.SeverityLevel.ERROR);
                violation.setAgentTrackingId(agentTrackingId);
                violation.setAgentName(agent.getFirstName() + " " + agent.getLastName());
                violation.setDetectionDate(LocalDateTime.now());
                violation.setMessage(String.format(
                    "Repos insuffisant: %s a terminé la mission d'escorte %s le %s. " +
                    "Repos de %d heures < minimum requis de %d heures",
                    violation.getAgentName(),
                    assignment.getEscortMission().getMissionNumber(),
                    escortEnd,
                    restHours,
                    rules.getMinRestHours()
                ));
                violation.setDetails(String.format("Dernière mission terminée le %s", escortEnd));
                violation.setCanBeOverridden(true);
                violations.add(violation);
            }
        }

        return violations;
    }

    @Override
    public List<RuleViolation> validateWeeklyMaxHours(
            UUID agentTrackingId,
            LocalDateTime weekStartDate,
            Long additionalHours
    ) {
        List<RuleViolation> violations = new ArrayList<>();
        ManagementRules rules = getActiveManagementRules();

        if (rules == null || rules.getMaxWeeklyHours() == 0) {
            return violations;
        }

        LocalDateTime weekEndDate = weekStartDate.plusWeeks(1);
        Long workedHours = calculateWorkedHours(agentTrackingId, weekStartDate, weekEndDate);
        Long totalHours = workedHours + (additionalHours != null ? additionalHours : 0);

        if (totalHours > rules.getMaxWeeklyHours()) {
            Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            RuleViolation violation = new RuleViolation();
            violation.setRuleType(RuleViolation.RuleType.WEEKLY_HOURS_EXCEEDED);
            violation.setSeverity(RuleViolation.SeverityLevel.WARNING);
            violation.setAgentTrackingId(agentTrackingId);
            violation.setAgentName(agent.getFirstName() + " " + agent.getLastName());
            violation.setDetectionDate(LocalDateTime.now());
            violation.setMessage(String.format(
                "Durée hebdomadaire dépassée: %s totalise %d heures (+ %d heures nouvelles) " +
                "pour la semaine du %s, maximum autorisé: %d heures",
                violation.getAgentName(),
                workedHours,
                additionalHours,
                weekStartDate.toLocalDate(),
                rules.getMaxWeeklyHours()
            ));
            violation.setDetails(String.format("Heures travaillées: %d, Heures supplémentaires: %d, Total: %d, Maximum: %d",
                    workedHours, additionalHours, totalHours, rules.getMaxWeeklyHours()));
            violation.setCanBeOverridden(true);
            violations.add(violation);
        }

        return violations;
    }

    @Override
    public List<RuleViolation> detectUnjustifiedAbsences() {
        List<RuleViolation> violations = new ArrayList<>();
        ManagementRules rules = getActiveManagementRules();

        if (rules == null || !rules.getAutoReportUnjustifiedAbsences()) {
            return violations;
        }

        // Récupérer les absences en attente sans justification
        List<Absences> pendingAbsences = absencesRepository.findByStatus(AbsenceStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();
        for (Absences absence : pendingAbsences) {
            // Si l'absence a commencé et n'a pas de justification
            if (absence.getStartDate().isBefore(now) &&
                (absence.getJustification() == null || absence.getJustification().trim().isEmpty())) {

                RuleViolation violation = new RuleViolation();
                violation.setRuleType(RuleViolation.RuleType.UNJUSTIFIED_ABSENCE);
                violation.setSeverity(RuleViolation.SeverityLevel.WARNING);
                violation.setAgentTrackingId(absence.getAgent().getTrackingId());
                violation.setAgentName(absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName());
                violation.setDetectionDate(LocalDateTime.now());
                violation.setMessage(String.format(
                    "Absence non justifiée: %s - Type: %s, Du %s au %s",
                    violation.getAgentName(),
                    absence.getAbsenceType(),
                    absence.getStartDate(),
                    absence.getEndDate()
                ));
                violation.setDetails(String.format("Raison: %s",
                    absence.getReason() != null ? absence.getReason() : "Non spécifiée"));
                violation.setCanBeOverridden(false);
                violations.add(violation);
            }
        }

        return violations;
    }

    @Override
    public List<RuleViolation> validateEquityDistribution(
            LocalDateTime periodStart,
            LocalDateTime periodEnd
    ) {
        List<RuleViolation> violations = new ArrayList<>();
        ManagementRules rules = getActiveManagementRules();

        if (rules == null || !rules.getEnforceEquityDistribution()) {
            return violations;
        }

        // Récupérer tous les agents actifs
        List<Agents> allAgents = agentsRepository.findAll();

        // Calculer les heures travaillées pour chaque agent
        Map<UUID, Long> agentHours = allAgents.stream()
                .collect(Collectors.toMap(
                        Agents::getTrackingId,
                        agent -> calculateWorkedHours(agent.getTrackingId(), periodStart, periodEnd)
                ));

        // Calculer la moyenne et l'écart-type
        double average = agentHours.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        double threshold = average * 0.5; // 50% de la moyenne comme seuil d'équité

        // Identifier les agents avec un déséquilibre significatif
        for (Map.Entry<UUID, Long> entry : agentHours.entrySet()) {
            long hours = entry.getValue();

            // Si l'agent travaille significativement plus ou moins que la moyenne
            if (Math.abs(hours - average) > threshold && average > 0) {
                Agents agent = agentsRepository.findByTrackingId(entry.getKey())
                        .orElse(null);

                if (agent != null) {
                    RuleViolation violation = new RuleViolation();
                    violation.setRuleType(RuleViolation.RuleType.EQUITY_DISTRIBUTION);
                    violation.setSeverity(RuleViolation.SeverityLevel.INFO);
                    violation.setAgentTrackingId(entry.getKey());
                    violation.setAgentName(agent.getFirstName() + " " + agent.getLastName());
                    violation.setDetectionDate(LocalDateTime.now());
                    violation.setMessage(String.format(
                        "Déséquilibre de répartition: %s a travaillé %d heures (moyenne: %.1f heures) " +
                        "pour la période du %s au %s",
                        violation.getAgentName(),
                        hours,
                        average,
                        periodStart.toLocalDate(),
                        periodEnd.toLocalDate()
                    ));
                    violation.setDetails(String.format("Écart: %.1f heures (%.1f%%)",
                            Math.abs(hours - average),
                            Math.abs(hours - average) / average * 100));
                    violation.setCanBeOverridden(true);
                    violations.add(violation);
                }
            }
        }

        return violations;
    }

    @Override
    public List<RuleViolation> validateAllRules(
            UUID agentTrackingId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            UUID currentMissionTrackingId
    ) {
        List<RuleViolation> allViolations = new ArrayList<>();

        // Règle 1: Pas de double affectation
        allViolations.addAll(validateNoDoubleAssignment(agentTrackingId, startDate, endDate, currentMissionTrackingId));

        // Règle 2: Repos minimal
        allViolations.addAll(validateMinimumRest(agentTrackingId, startDate));

        // Règle 3: Durée hebdomadaire maximale
        LocalDateTime weekStart = startDate.truncatedTo(ChronoUnit.DAYS)
                .minusDays(startDate.getDayOfWeek().getValue() - 1);
        long missionDurationHours = Duration.between(startDate, endDate != null ? endDate : startDate.plusDays(1)).toHours();
        allViolations.addAll(validateWeeklyMaxHours(agentTrackingId, weekStart, missionDurationHours));

        log.info("✅ Validation complète des règles pour l'agent {} : {} violation(s) détectée(s)",
                agentTrackingId, allViolations.size());

        return allViolations;
    }

    @Override
    public Long calculateWorkedHours(UUID agentTrackingId, LocalDateTime startDate, LocalDateTime endDate) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        long totalHours = 0;

        // Calculer les heures ArmedGuard
        List<ArmedGuardPersonnels> armedGuardAssignments = armedGuardPersonnelRepository.findByAgentId(agent.getId());
        for (ArmedGuardPersonnels assignment : armedGuardAssignments) {
            if (assignment.getEmbarkationDate() != null && assignment.getDisembarkationDate() != null) {
                // Vérifier si la mission chevauche la période
                if (periodsOverlap(startDate, endDate, assignment.getEmbarkationDate(), assignment.getDisembarkationDate())) {
                    // Calculer l'intersection des périodes
                    LocalDateTime intersectionStart = assignment.getEmbarkationDate().isAfter(startDate)
                            ? assignment.getEmbarkationDate() : startDate;
                    LocalDateTime intersectionEnd = assignment.getDisembarkationDate().isBefore(endDate)
                            ? assignment.getDisembarkationDate() : endDate;

                    totalHours += Duration.between(intersectionStart, intersectionEnd).toHours();
                }
            }
        }

        // Calculer les heures Escort
        List<EscortPersonnels> escortAssignments = escortPersonnelRepository.findRealMissionsByAgentId(agent.getId());
        for (EscortPersonnels assignment : escortAssignments) {
            EscortMissions mission = assignment.getEscortMission();
            if (mission.getStartDate() != null && mission.getEndDate() != null) {
                if (periodsOverlap(startDate, endDate, mission.getStartDate(), mission.getEndDate())) {
                    LocalDateTime intersectionStart = mission.getStartDate().isAfter(startDate)
                            ? mission.getStartDate() : startDate;
                    LocalDateTime intersectionEnd = mission.getEndDate().isBefore(endDate)
                            ? mission.getEndDate() : endDate;

                    totalHours += Duration.between(intersectionStart, intersectionEnd).toHours();
                }
            }
        }

        return totalHours;
    }

    @Override
    public ManagementRules getActiveManagementRules() {
        // Récupérer la règle active la plus récente
        List<ManagementRules> allRules = managementRulesRepository.findAll();

        if (allRules.isEmpty()) {
            log.warn("⚠️ Aucune règle de gestion n'est définie dans le système");
            return null;
        }

        // Retourner la règle la plus récente qui est effective
        return allRules.stream()
                .filter(rule -> rule.getEffectiveDate().isBefore(LocalDateTime.now()) ||
                               rule.getEffectiveDate().isEqual(LocalDateTime.now()))
                .max((r1, r2) -> r1.getEffectiveDate().compareTo(r2.getEffectiveDate()))
                .orElse(allRules.get(0));
    }

    /**
     * Vérifie si deux périodes se chevauchent
     */
    private boolean periodsOverlap(LocalDateTime start1, LocalDateTime end1,
                                   LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
}
