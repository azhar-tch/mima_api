package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ArmedGuardMissionRequest;
import com.helpysoft.mima_api.dto.ArmedGuardMissionResponse;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.service.ArmedGuardMissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/armed-guard-missions")
@RequiredArgsConstructor
@Tag(name = "Armed Guard Missions", description = "Gestion des missions de garde armée à bord des navires de commerce")
public class ArmedGuardMissionController {

    private final ArmedGuardMissionService armedGuardMissionService;

    @PostMapping("/create")
    @Operation(summary = "Créer une mission de garde armée", description = "Enregistrer une nouvelle mission de garde armée (embarquement)")
    public ResponseEntity<Map<String, Object>> createMission(@RequestBody ArmedGuardMissionRequest request) {
        try {
            ArmedGuardMissionResponse response = armedGuardMissionService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission de garde armée créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la mission", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour une mission", description = "Modifier les informations d'une mission de garde armée (y compris débarquement)")
    public ResponseEntity<Map<String, Object>> updateMission(@PathVariable UUID trackingId, @RequestBody ArmedGuardMissionRequest request) {
        try {
            ArmedGuardMissionResponse response = armedGuardMissionService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la mission", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer une mission", description = "Obtenir les détails complets d'une mission par son trackingId")
    public ResponseEntity<Map<String, Object>> getMission(@PathVariable UUID trackingId) {
        try {
            ArmedGuardMissionResponse response = armedGuardMissionService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Mission non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/mission-number/{missionNumber}")
    @Operation(summary = "Récupérer par numéro de mission", description = "Obtenir une mission par son numéro d'identification")
    public ResponseEntity<Map<String, Object>> getByMissionNumber(@PathVariable String missionNumber) {
        try {
            ArmedGuardMissionResponse response = armedGuardMissionService.findByMissionNumber(missionNumber);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Mission non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/status/{status}")
    @Operation(summary = "Lister par statut", description = "Récupérer toutes les missions selon leur statut (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED)")
    public ResponseEntity<Map<String, Object>> listByStatus(@PathVariable MissionStatus status) {
        try {
            List<ArmedGuardMissionResponse> responses = armedGuardMissionService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/period")
    @Operation(summary = "Lister par période", description = "Récupérer les missions entre deux dates (par date d'embarquement)")
    public ResponseEntity<Map<String, Object>> listByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ArmedGuardMissionResponse> responses = armedGuardMissionService.findByPeriod(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/commercial-ship/{shipTrackingId}")
    @Operation(summary = "Lister par navire commercial", description = "Récupérer toutes les gardes armées d'un navire commercial spécifique")
    public ResponseEntity<Map<String, Object>> listByCommercialShip(@PathVariable UUID shipTrackingId) {
        try {
            List<ArmedGuardMissionResponse> responses = armedGuardMissionService.findByCommercialShip(shipTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/security-agency/{agencyTrackingId}")
    @Operation(summary = "Lister par agence de sécurité", description = "Récupérer toutes les gardes armées demandées par une agence")
    public ResponseEntity<Map<String, Object>> listBySecurityAgency(@PathVariable UUID agencyTrackingId) {
        try {
            List<ArmedGuardMissionResponse> responses = armedGuardMissionService.findBySecurityAgency(agencyTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister toutes les missions", description = "Récupérer la liste complète de toutes les missions de garde armée")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<ArmedGuardMissionResponse> responses = armedGuardMissionService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer une mission", description = "Supprimer définitivement une mission de garde armée du système")
    public ResponseEntity<Map<String, Object>> deleteMission(@PathVariable UUID trackingId) {
        try {
            armedGuardMissionService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la mission", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
