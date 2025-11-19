package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ShipIncidentRequest;
import com.helpysoft.mima_api.dto.ShipIncidentResponse;
import com.helpysoft.mima_api.service.ShipIncidentService;
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
@RequestMapping("/api/ship-incidents")
@RequiredArgsConstructor
@Tag(name = "Ship Incidents", description = "Gestion des incidents et opérations d'assistance maritimes")
public class ShipIncidentController {

    private final ShipIncidentService shipIncidentService;

    @PostMapping("/create")
    @Operation(summary = "Enregistrer un incident", description = "Créer un nouvel enregistrement d'incident ou d'assistance maritime")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ShipIncidentRequest request) {
        try {
            ShipIncidentResponse response = shipIncidentService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incident enregistré avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de l'enregistrement", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour un incident", description = "Modifier les informations d'un incident maritime")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody ShipIncidentRequest request) {
        try {
            ShipIncidentResponse response = shipIncidentService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incident mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer un incident", description = "Obtenir les détails d'un incident par son trackingId")
    public ResponseEntity<Map<String, Object>> getByTrackingId(@PathVariable UUID trackingId) {
        try {
            ShipIncidentResponse response = shipIncidentService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incident récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Incident non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/commercial-ship/{shipTrackingId}")
    @Operation(summary = "Lister par navire", description = "Récupérer tous les incidents d'un navire commercial")
    public ResponseEntity<Map<String, Object>> listByCommercialShip(@PathVariable UUID shipTrackingId) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findByCommercialShip(shipTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/ongoing")
    @Operation(summary = "Incidents en cours", description = "Liste des incidents maritimes actuellement en cours")
    public ResponseEntity<Map<String, Object>> listOngoingIncidents() {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findOngoingIncidents();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents en cours récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/status/{status}")
    @Operation(summary = "Lister par statut", description = "Récupérer les incidents selon leur statut (EN_COURS, RESOLU, EN_INVESTIGATION)")
    public ResponseEntity<Map<String, Object>> listByStatus(@PathVariable String status) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/period/{startDate}/{endDate}")
    @Operation(summary = "Lister par période", description = "Récupérer les incidents survenus entre deux dates")
    public ResponseEntity<Map<String, Object>> listByPeriod(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findByIncidentDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/event-type/{eventType}")
    @Operation(summary = "Lister par type d'événement", description = "Récupérer les incidents par type d'événement (INCIDENT ou ASSISTANCE)")
    public ResponseEntity<Map<String, Object>> listByEventType(@PathVariable String eventType) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findByEventType(eventType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/severity/{severity}")
    @Operation(summary = "Lister par sévérité", description = "Récupérer les incidents par niveau de sévérité (FAIBLE, MOYENNE, GRAVE, CRITIQUE)")
    public ResponseEntity<Map<String, Object>> listBySeverity(@PathVariable String severity) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findBySeverity(severity);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/with-pollution")
    @Operation(summary = "Incidents avec pollution", description = "Récupérer les incidents ayant causé une pollution")
    public ResponseEntity<Map<String, Object>> listIncidentsWithPollution() {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findIncidentsWithPollution();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents avec pollution récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/maritime-zone/{maritimeZone}")
    @Operation(summary = "Lister par zone maritime", description = "Récupérer les incidents par zone maritime")
    public ResponseEntity<Map<String, Object>> listByMaritimeZone(@PathVariable String maritimeZone) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findByMaritimeZone(maritimeZone);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister tous les incidents", description = "Récupérer la liste complète de tous les incidents maritimes")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incidents récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des incidents", description = "Rechercher des incidents par nom de navire, type d'événement, statut, zone maritime, etc.")
    public ResponseEntity<Map<String, Object>> searchShipIncidents(@RequestParam(required = false) String term) {
        try {
            List<ShipIncidentResponse> responses = shipIncidentService.searchShipIncidents(term);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Recherche effectuée avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des incidents", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un incident", description = "Supprimer définitivement un enregistrement d'incident")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            shipIncidentService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Incident supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
