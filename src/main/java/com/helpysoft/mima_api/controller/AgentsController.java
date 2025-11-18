package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.service.AgentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Agents", description = "Gestion des agents - personnel maritime")
public class AgentsController {

    private final AgentsService agentsService;

    @PostMapping("/create")
    @Operation(summary = "Créer un agent", description = "Enregistrer un nouvel agent avec ses informations maritimes (grade, spécialité, certificats, etc.)")
    public ResponseEntity<Map<String, Object>> createAgent(@Valid @RequestBody AgentsRequest request) {
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
    @Operation(summary = "Mettre à jour un agent", description = "Modifier les informations d'un agent existant")
    public ResponseEntity<Map<String, Object>> updateAgent(@PathVariable UUID trackingId, @Valid @RequestBody AgentsRequest request) {
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
    @Operation(summary = "Récupérer un agent", description = "Obtenir les détails complets d'un agent par son trackingId")
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
    @Operation(summary = "Lister les agents par unité", description = "Récupérer tous les agents affectés à une unité spécifique")
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
    @Operation(summary = "Lister tous les agents", description = "Récupérer la liste complète de tous les agents enregistrés")
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

    @GetMapping("/search")
    @Operation(summary = "Rechercher des agents", description = "Rechercher des agents par nom, prénom, matricule, email ou numéro de livret maritime")
    public ResponseEntity<Map<String, Object>> searchAgents(@RequestParam(required = false) String term) {
        try {
            List<AgentsResponse> responses = agentsService.searchAgents(term);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Recherche effectuée avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des agents", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un agent", description = "Supprimer définitivement un agent du système")
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
