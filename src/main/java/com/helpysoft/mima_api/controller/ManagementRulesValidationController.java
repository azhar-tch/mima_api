package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.service.ManagementRulesValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller pour la validation des règles de gestion
 */
@RestController
@RequestMapping("/api/management-rules/validate")
@RequiredArgsConstructor
public class ManagementRulesValidationController {

    private final ManagementRulesValidationService validationService;

    /**
     * Valide une affectation contre toutes les règles
     */
    @PostMapping("/assignment")
    public ResponseEntity<Map<String, Object>> validateAssignment(
            @RequestParam UUID agentTrackingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) UUID currentMissionTrackingId
    ) {
        try {
            List<RuleViolation> violations = validationService.validateAllRules(
                    agentTrackingId,
                    startDate,
                    endDate,
                    currentMissionTrackingId
            );

            boolean hasErrors = violations.stream()
                    .anyMatch(v -> v.getSeverity() == RuleViolation.SeverityLevel.ERROR ||
                                  v.getSeverity() == RuleViolation.SeverityLevel.CRITICAL);

            String message = violations.isEmpty()
                    ? "Aucune violation détectée. L'affectation respecte toutes les règles."
                    : String.format("%d violation(s) détectée(s)", violations.size());

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            hasErrors,
                            message,
                            Map.of(
                                    "violations", violations,
                                    "canProceed", !hasErrors,
                                    "warningsOnly", !hasErrors && !violations.isEmpty()
                            ),
                            ""
                    ),
                    hasErrors ? HttpStatus.BAD_REQUEST : HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la validation", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Vérifie la règle de double affectation
     */
    @GetMapping("/double-assignment")
    public ResponseEntity<Map<String, Object>> checkDoubleAssignment(
            @RequestParam UUID agentTrackingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) UUID currentMissionTrackingId
    ) {
        try {
            List<RuleViolation> violations = validationService.validateNoDoubleAssignment(
                    agentTrackingId, startDate, endDate, currentMissionTrackingId
            );

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            !violations.isEmpty(),
                            violations.isEmpty() ? "Aucune double affectation détectée" : "Double affectation détectée",
                            violations,
                            ""
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la vérification", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Vérifie le repos minimal
     */
    @GetMapping("/minimum-rest")
    public ResponseEntity<Map<String, Object>> checkMinimumRest(
            @RequestParam UUID agentTrackingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newMissionStartDate
    ) {
        try {
            List<RuleViolation> violations = validationService.validateMinimumRest(
                    agentTrackingId, newMissionStartDate
            );

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            !violations.isEmpty(),
                            violations.isEmpty() ? "Repos suffisant" : "Repos insuffisant",
                            violations,
                            ""
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la vérification", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Vérifie la durée hebdomadaire maximale
     */
    @GetMapping("/weekly-hours")
    public ResponseEntity<Map<String, Object>> checkWeeklyHours(
            @RequestParam UUID agentTrackingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime weekStartDate,
            @RequestParam Long additionalHours
    ) {
        try {
            List<RuleViolation> violations = validationService.validateWeeklyMaxHours(
                    agentTrackingId, weekStartDate, additionalHours
            );

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            !violations.isEmpty(),
                            violations.isEmpty() ? "Durée hebdomadaire respectée" : "Durée hebdomadaire dépassée",
                            violations,
                            ""
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la vérification", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Détecte les absences non justifiées
     */
    @GetMapping("/unjustified-absences")
    public ResponseEntity<Map<String, Object>> detectUnjustifiedAbsences() {
        try {
            List<RuleViolation> violations = validationService.detectUnjustifiedAbsences();

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            false,
                            String.format("%d absence(s) non justifiée(s) détectée(s)", violations.size()),
                            violations,
                            ""
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la détection", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Vérifie l'équité de répartition
     */
    @GetMapping("/equity-distribution")
    public ResponseEntity<Map<String, Object>> checkEquityDistribution(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodEnd
    ) {
        try {
            List<RuleViolation> violations = validationService.validateEquityDistribution(
                    periodStart, periodEnd
            );

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            false,
                            String.format("%d déséquilibre(s) détecté(s)", violations.size()),
                            violations,
                            ""
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la vérification", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Calcule les heures travaillées par un agent
     */
    @GetMapping("/worked-hours")
    public ResponseEntity<Map<String, Object>> getWorkedHours(
            @RequestParam UUID agentTrackingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        try {
            Long hours = validationService.calculateWorkedHours(agentTrackingId, startDate, endDate);

            return new ResponseEntity<>(
                    Helper.responseFormat(
                            false,
                            "Heures travaillées calculées avec succès",
                            Map.of(
                                    "agentTrackingId", agentTrackingId,
                                    "periodStart", startDate,
                                    "periodEnd", endDate,
                                    "workedHours", hours
                            ),
                            ""
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors du calcul", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
