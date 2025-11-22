package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.RuleViolationAlert;
import com.helpysoft.mima_api.service.RuleViolationAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/rule-violation-alerts")
@RequiredArgsConstructor
public class RuleViolationAlertController {

    private final RuleViolationAlertService alertService;

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveAlerts() {
        try {
            List<RuleViolationAlert> alerts = alertService.findActiveAlerts();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes actives récupérées avec succès", alerts, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/critical")
    public ResponseEntity<Map<String, Object>> getCriticalActiveAlerts() {
        try {
            List<RuleViolationAlert> alerts = alertService.findCriticalActiveAlerts();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes critiques récupérées avec succès", alerts, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes critiques", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/by-agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> getAlertsByAgent(@PathVariable UUID agentTrackingId) {
        try {
            List<RuleViolationAlert> alerts = alertService.findByAgent(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes de l'agent récupérées avec succès", alerts, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes de l'agent", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<Map<String, Object>> getAlertsByStatus(@PathVariable RuleViolationAlert.AlertStatus status) {
        try {
            List<RuleViolationAlert> alerts = alertService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes récupérées avec succès", alerts, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/by-rule-type/{ruleType}")
    public ResponseEntity<Map<String, Object>> getAlertsByRuleType(@PathVariable RuleViolation.RuleType ruleType) {
        try {
            List<RuleViolationAlert> alerts = alertService.findByRuleType(ruleType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes récupérées avec succès", alerts, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/by-period")
    public ResponseEntity<Map<String, Object>> getAlertsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        try {
            List<RuleViolationAlert> alerts = alertService.findByPeriod(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes récupérées avec succès", alerts, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<Map<String, Object>> getAlert(@PathVariable UUID trackingId) {
        try {
            RuleViolationAlert alert = alertService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte récupérée avec succès", alert, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Alerte non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping("/{trackingId}/resolve")
    public ResponseEntity<Map<String, Object>> resolveAlert(
            @PathVariable UUID trackingId,
            @RequestParam UUID resolvedByUserId,
            @RequestParam(required = false) String comment
    ) {
        try {
            RuleViolationAlert alert = alertService.resolveAlert(trackingId, resolvedByUserId, comment);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte résolue avec succès", alert, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la résolution de l'alerte", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/{trackingId}/override")
    public ResponseEntity<Map<String, Object>> overrideAlert(
            @PathVariable UUID trackingId,
            @RequestParam UUID overriddenByUserId,
            @RequestParam(required = false) String comment
    ) {
        try {
            RuleViolationAlert alert = alertService.overrideAlert(trackingId, overriddenByUserId, comment);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte annulée avec succès", alert, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de l'annulation de l'alerte", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/{trackingId}/dismiss")
    public ResponseEntity<Map<String, Object>> dismissAlert(
            @PathVariable UUID trackingId,
            @RequestParam UUID dismissedByUserId,
            @RequestParam(required = false) String comment
    ) {
        try {
            RuleViolationAlert alert = alertService.dismissAlert(trackingId, dismissedByUserId, comment);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte rejetée avec succès", alert, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors du rejet de l'alerte", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/count/active")
    public ResponseEntity<Map<String, Object>> countActiveAlerts() {
        try {
            Long count = alertService.countActiveAlerts();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Nombre d'alertes actives récupéré avec succès", count, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors du comptage des alertes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/count/critical")
    public ResponseEntity<Map<String, Object>> countCriticalActiveAlerts() {
        try {
            Long count = alertService.countCriticalActiveAlerts();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Nombre d'alertes critiques récupéré avec succès", count, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors du comptage des alertes critiques", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
