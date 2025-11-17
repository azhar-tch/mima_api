package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentAwardHistoryRequest;
import com.helpysoft.mima_api.dto.AgentAwardHistoryResponse;
import com.helpysoft.mima_api.service.AgentAwardHistoryService;
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
@RequestMapping("/api/agent-award-history")
@RequiredArgsConstructor
public class AgentAwardHistoryController {

    private final AgentAwardHistoryService agentAwardHistoryService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AgentAwardHistoryRequest request) {
        try {
            AgentAwardHistoryResponse response = agentAwardHistoryService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de distinction créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'historique de distinction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AgentAwardHistoryRequest request) {
        try {
            AgentAwardHistoryResponse response = agentAwardHistoryService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de distinction mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'historique de distinction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AgentAwardHistoryResponse response = agentAwardHistoryService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de distinction récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Historique de distinction non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            List<AgentAwardHistoryResponse> responses = agentAwardHistoryService.findByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique des distinctions de l'agent récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/award/{awardTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAwardTrackingId(@PathVariable UUID awardTrackingId) {
        try {
            List<AgentAwardHistoryResponse> responses = agentAwardHistoryService.findByAwardTrackingId(awardTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de la distinction récupéré avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/award-date-range/{startDate}/{endDate}")
    public ResponseEntity<Map<String, Object>> findByAwardDateBetween(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<AgentAwardHistoryResponse> responses = agentAwardHistoryService.findByAwardDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinctions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des distinctions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}/count")
    public ResponseEntity<Map<String, Object>> countByAgentTrackingId(@PathVariable UUID agentTrackingId) {
        try {
            Long count = agentAwardHistoryService.countByAgentTrackingId(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Nombre de distinctions récupéré avec succès", count, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors du comptage des distinctions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AgentAwardHistoryResponse> responses = agentAwardHistoryService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques des distinctions récupérés avec succès", responses, ""),
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
            agentAwardHistoryService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique de distinction supprimé avec succès", null, ""),
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
