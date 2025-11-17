package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ShipArrivalDepartureRequest;
import com.helpysoft.mima_api.dto.ShipArrivalDepartureResponse;
import com.helpysoft.mima_api.service.ShipArrivalDepartureService;
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
@RequestMapping("/api/ship-arrivals-departures")
@RequiredArgsConstructor
@Tag(name = "Ship Arrivals/Departures", description = "Gestion des arrivées et départs des navires au Port de Lomé")
public class ShipArrivalDepartureController {

    private final ShipArrivalDepartureService arrivalDepartureService;

    @PostMapping("/create")
    @Operation(summary = "Enregistrer une arrivée/départ", description = "Créer un nouvel enregistrement d'arrivée ou de départ de navire")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ShipArrivalDepartureRequest request) {
        try {
            ShipArrivalDepartureResponse response = arrivalDepartureService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Arrivée/Départ enregistré avec succès", response, ""),
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
    @Operation(summary = "Mettre à jour un enregistrement", description = "Modifier les informations d'arrivée/départ d'un navire")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody ShipArrivalDepartureRequest request) {
        try {
            ShipArrivalDepartureResponse response = arrivalDepartureService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrement mis à jour avec succès", response, ""),
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
    @Operation(summary = "Récupérer un enregistrement", description = "Obtenir les détails d'un enregistrement d'arrivée/départ par son trackingId")
    public ResponseEntity<Map<String, Object>> getByTrackingId(@PathVariable UUID trackingId) {
        try {
            ShipArrivalDepartureResponse response = arrivalDepartureService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrement récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Enregistrement non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/commercial-ship/{shipTrackingId}")
    @Operation(summary = "Lister par navire", description = "Récupérer tous les enregistrements d'un navire commercial")
    public ResponseEntity<Map<String, Object>> listByCommercialShip(@PathVariable UUID shipTrackingId) {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findByCommercialShip(shipTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/currently-in-port")
    @Operation(summary = "Navires actuellement au port", description = "Liste des navires actuellement présents au Port de Lomé")
    public ResponseEntity<Map<String, Object>> listCurrentlyInPort() {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findShipsCurrentlyInPort();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires au port récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/arrivals")
    @Operation(summary = "Lister par période d'arrivée", description = "Récupérer les navires arrivés entre deux dates")
    public ResponseEntity<Map<String, Object>> listByArrivalPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findByArrivalDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Arrivées récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/departures")
    @Operation(summary = "Lister par période de départ", description = "Récupérer les navires partis entre deux dates")
    public ResponseEntity<Map<String, Object>> listByDeparturePeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findByDepartureDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Départs récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/port-of-origin/{portOfOrigin}")
    @Operation(summary = "Lister par port de provenance", description = "Récupérer les navires venus d'un port spécifique")
    public ResponseEntity<Map<String, Object>> listByPortOfOrigin(@PathVariable String portOfOrigin) {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findByPortOfOrigin(portOfOrigin);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/next-destination/{nextDestination}")
    @Operation(summary = "Lister par destination", description = "Récupérer les navires se dirigeant vers une destination spécifique")
    public ResponseEntity<Map<String, Object>> listByNextDestination(@PathVariable String nextDestination) {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findByNextDestination(nextDestination);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrements récupérés avec succès", responses, ""),
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
    @Operation(summary = "Lister tous les enregistrements", description = "Récupérer la liste complète de tous les enregistrements")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<ShipArrivalDepartureResponse> responses = arrivalDepartureService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un enregistrement", description = "Supprimer définitivement un enregistrement d'arrivée/départ")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            arrivalDepartureService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Enregistrement supprimé avec succès", null, ""),
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
