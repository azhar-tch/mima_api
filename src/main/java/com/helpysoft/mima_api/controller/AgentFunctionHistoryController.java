package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentFunctionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentFunctionHistoryResponse;
import com.helpysoft.mima_api.service.AgentFunctionHistoryService;
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
@RequestMapping("/api/agent-function-history")
@RequiredArgsConstructor
public class AgentFunctionHistoryController {

    private final AgentFunctionHistoryService agentFunctionHistoryService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AgentFunctionHistoryRequest request) {
        try {
            AgentFunctionHistoryResponse response = agentFunctionHistoryService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de fonction créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'historique de fonction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AgentFunctionHistoryRequest request) {
        try {
            AgentFunctionHistoryResponse response = agentFunctionHistoryService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de fonction mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'historique de fonction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AgentFunctionHistoryResponse response = agentFunctionHistoryService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de fonction récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Historique de fonction non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            List<AgentFunctionHistoryResponse> responses = agentFunctionHistoryService.findByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique des fonctions de l'agent récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/function/{functionTrackingId}")
    public ResponseEntity<Map<String, Object>> findByFunctionTrackingId(@PathVariable UUID functionTrackingId) {
        try {
            List<AgentFunctionHistoryResponse> responses = agentFunctionHistoryService.findByFunctionTrackingId(functionTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de la fonction récupéré avec succès", responses, ""),
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
            List<AgentFunctionHistoryResponse> responses = agentFunctionHistoryService.findByStartDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonctions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des fonctions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}/current")
    public ResponseEntity<Map<String, Object>> findCurrentFunctionByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            AgentFunctionHistoryResponse response = agentFunctionHistoryService.findCurrentFunctionByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonction actuelle de l'agent récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Fonction actuelle non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AgentFunctionHistoryResponse> responses = agentFunctionHistoryService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques des fonctions récupérés avec succès", responses, ""),
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
            agentFunctionHistoryService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de fonction supprimé avec succès", null, ""),
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
