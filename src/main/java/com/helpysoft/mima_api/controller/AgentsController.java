package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.service.AgentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentsController {

    private final AgentsService agentsService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAgent(@RequestBody AgentsRequest request) {
        try {
            AgentsResponse response = agentsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agent créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'agent", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> updateAgent(@PathVariable UUID trackingId, @RequestBody AgentsRequest request) {
        try {
            AgentsResponse response = agentsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agent mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'agent", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> getAgent(@PathVariable UUID trackingId) {
        try {
            AgentsResponse response = agentsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agent récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Agent non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/unit/{unitTrackingId}")
    public ResponseEntity<Map<String, Object>> listAgentsByUnit(@PathVariable UUID unitTrackingId) {
        try {
            List<AgentsResponse> responses = agentsService.findByUnit(unitTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des agents", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listAgents() {
        try {
            List<AgentsResponse> responses = agentsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des agents", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> deleteAgent(@PathVariable UUID trackingId) {
        try {
            agentsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agent supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'agent", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
