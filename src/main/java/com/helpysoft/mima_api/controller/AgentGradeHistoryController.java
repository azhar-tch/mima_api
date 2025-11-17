package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentGradeHistoryRequest;
import com.helpysoft.mima_api.dto.AgentGradeHistoryResponse;
import com.helpysoft.mima_api.service.AgentGradeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/agent-grade-history")
@RequiredArgsConstructor
public class AgentGradeHistoryController {

    private final AgentGradeHistoryService agentGradeHistoryService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AgentGradeHistoryRequest request) {
        try {
            AgentGradeHistoryResponse response = agentGradeHistoryService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de grade créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'historique de grade", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AgentGradeHistoryRequest request) {
        try {
            AgentGradeHistoryResponse response = agentGradeHistoryService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de grade mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'historique de grade", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AgentGradeHistoryResponse response = agentGradeHistoryService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de grade récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Historique de grade non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            List<AgentGradeHistoryResponse> responses = agentGradeHistoryService.findByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique des grades de l'agent récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/grade/{gradeTrackingId}")
    public ResponseEntity<Map<String, Object>> findByGradeTrackingId(@PathVariable UUID gradeTrackingId) {
        try {
            List<AgentGradeHistoryResponse> responses = agentGradeHistoryService.findByGradeTrackingId(gradeTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique du grade récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/promotion-date-range")
    public ResponseEntity<Map<String, Object>> findByPromotionDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<AgentGradeHistoryResponse> responses = agentGradeHistoryService.findByPromotionDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Promotions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des promotions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}/latest")
    public ResponseEntity<Map<String, Object>> findLatestGradeByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            AgentGradeHistoryResponse response = agentGradeHistoryService.findLatestGradeByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grade actuel de l'agent récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Grade actuel non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AgentGradeHistoryResponse> responses = agentGradeHistoryService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques des grades récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            agentGradeHistoryService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de grade supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
