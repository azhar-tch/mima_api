package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.service.AgentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Marins", description = "Gestion des marins - personnel maritime")
public class AgentsController {

    private final AgentsService agentsService;

    @PostMapping("/create")
    @Operation(summary = "Créer un marin", description = "Enregistrer un nouveau marin avec ses informations maritimes (grade, spécialité, certificats, etc.)")
    public ResponseEntity<Map<String, Object>> createAgent(@RequestBody AgentsRequest request) {
        try {
            AgentsResponse response = agentsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Marin créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création du marin", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour un marin", description = "Modifier les informations d'un marin existant")
    public ResponseEntity<Map<String, Object>> updateAgent(@PathVariable UUID trackingId, @RequestBody AgentsRequest request) {
        try {
            AgentsResponse response = agentsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Marin mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour du marin", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer un marin", description = "Obtenir les détails complets d'un marin par son trackingId")
    public ResponseEntity<Map<String, Object>> getAgent(@PathVariable UUID trackingId) {
        try {
            AgentsResponse response = agentsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Marin récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Marin non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/unit/{unitTrackingId}")
    @Operation(summary = "Lister les marins par unité", description = "Récupérer tous les marins affectés à une unité spécifique")
    public ResponseEntity<Map<String, Object>> listAgentsByUnit(@PathVariable UUID unitTrackingId) {
        try {
            List<AgentsResponse> responses = agentsService.findByUnit(unitTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Marins récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des marins", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister tous les marins", description = "Récupérer la liste complète de tous les marins enregistrés")
    public ResponseEntity<Map<String, Object>> listAgents() {
        try {
            List<AgentsResponse> responses = agentsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Marins récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des marins", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un marin", description = "Supprimer définitivement un marin du système")
    public ResponseEntity<Map<String, Object>> deleteAgent(@PathVariable UUID trackingId) {
        try {
            agentsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Marin supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression du marin", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
