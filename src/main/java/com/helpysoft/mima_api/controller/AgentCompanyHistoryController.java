package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentCompanyHistoryRequest;
import com.helpysoft.mima_api.dto.AgentCompanyHistoryResponse;
import com.helpysoft.mima_api.service.AgentCompanyHistoryService;
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
@RequestMapping("/api/agent-company-history")
@RequiredArgsConstructor
public class AgentCompanyHistoryController {

    private final AgentCompanyHistoryService agentCompanyHistoryService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AgentCompanyHistoryRequest request) {
        try {
            AgentCompanyHistoryResponse response = agentCompanyHistoryService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de compagnie créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'historique de compagnie", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AgentCompanyHistoryRequest request) {
        try {
            AgentCompanyHistoryResponse response = agentCompanyHistoryService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de compagnie mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'historique de compagnie", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AgentCompanyHistoryResponse response = agentCompanyHistoryService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de compagnie récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Historique de compagnie non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            List<AgentCompanyHistoryResponse> responses = agentCompanyHistoryService.findByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique des compagnies de l'agent récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/company/{companyTrackingId}")
    public ResponseEntity<Map<String, Object>> findByCompanyTrackingId(@PathVariable UUID companyTrackingId) {
        try {
            List<AgentCompanyHistoryResponse> responses = agentCompanyHistoryService.findByCompanyTrackingId(companyTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de la compagnie récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/start-date-range")
    public ResponseEntity<Map<String, Object>> findByStartDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<AgentCompanyHistoryResponse> responses = agentCompanyHistoryService.findByStartDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectations de compagnie récupérées avec succès", responses, ""),
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
    public ResponseEntity<Map<String, Object>> findCurrentCompanyByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            AgentCompanyHistoryResponse response = agentCompanyHistoryService.findCurrentCompanyByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnie actuelle de l'agent récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Compagnie actuelle non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AgentCompanyHistoryResponse> responses = agentCompanyHistoryService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques des compagnies récupérés avec succès", responses, ""),
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
            agentCompanyHistoryService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de compagnie supprimé avec succès", null, ""),
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
