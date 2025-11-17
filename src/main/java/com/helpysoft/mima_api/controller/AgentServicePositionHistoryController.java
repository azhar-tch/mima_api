package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentServicePositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentServicePositionHistoryResponse;
import com.helpysoft.mima_api.service.AgentServicePositionHistoryService;
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
@RequestMapping("/api/agent-service-position-history")
@RequiredArgsConstructor
public class AgentServicePositionHistoryController {

    private final AgentServicePositionHistoryService agentServicePositionHistoryService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AgentServicePositionHistoryRequest request) {
        try {
            AgentServicePositionHistoryResponse response = agentServicePositionHistoryService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique d'affectation créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'historique d'affectation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AgentServicePositionHistoryRequest request) {
        try {
            AgentServicePositionHistoryResponse response = agentServicePositionHistoryService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique d'affectation mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'historique d'affectation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AgentServicePositionHistoryResponse response = agentServicePositionHistoryService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique d'affectation récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Historique d'affectation non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            List<AgentServicePositionHistoryResponse> responses = agentServicePositionHistoryService.findByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique des affectations de l'agent récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/service-position/{positionTrackingId}")
    public ResponseEntity<Map<String, Object>> findByServicePositionTrackingId(@PathVariable UUID positionTrackingId) {
        try {
            List<AgentServicePositionHistoryResponse> responses = agentServicePositionHistoryService.findByServicePositionTrackingId(positionTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de l'affectation récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/start-date-range/{startDate}/{endDate}")
    public ResponseEntity<Map<String, Object>> findByStartDateBetween(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<AgentServicePositionHistoryResponse> responses = agentServicePositionHistoryService.findByStartDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des affectations", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}/current")
    public ResponseEntity<Map<String, Object>> findCurrentPositionByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            AgentServicePositionHistoryResponse response = agentServicePositionHistoryService.findCurrentPositionByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectation actuelle de l'agent récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Affectation actuelle non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AgentServicePositionHistoryResponse> responses = agentServicePositionHistoryService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques des affectations récupérés avec succès", responses, ""),
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
            agentServicePositionHistoryService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique d'affectation supprimé avec succès", null, ""),
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
