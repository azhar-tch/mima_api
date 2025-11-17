package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.CommercialShipRequest;
import com.helpysoft.mima_api.dto.CommercialShipResponse;
import com.helpysoft.mima_api.service.CommercialShipService;
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
@RequestMapping("/api/commercial-ships")
@RequiredArgsConstructor
@Tag(name = "Commercial Ships", description = "Gestion des navires de commerce")
public class CommercialShipController {

    private final CommercialShipService commercialShipService;

    @PostMapping("/create")
    @Operation(summary = "Créer un navire de commerce", description = "Enregistrer un nouveau navire de commerce avec son numéro IMO unique")
    public ResponseEntity<Map<String, Object>> createShip(@RequestBody CommercialShipRequest request) {
        try {
            CommercialShipResponse response = commercialShipService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navire de commerce créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création du navire", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour un navire", description = "Modifier les informations d'un navire de commerce existant")
    public ResponseEntity<Map<String, Object>> updateShip(@PathVariable UUID trackingId, @RequestBody CommercialShipRequest request) {
        try {
            CommercialShipResponse response = commercialShipService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navire mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour du navire", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer un navire", description = "Obtenir les détails complets d'un navire par son trackingId")
    public ResponseEntity<Map<String, Object>> getShip(@PathVariable UUID trackingId) {
        try {
            CommercialShipResponse response = commercialShipService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navire récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Navire non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/imo/{imoNumber}")
    @Operation(summary = "Récupérer par numéro IMO", description = "Obtenir un navire par son numéro IMO unique")
    public ResponseEntity<Map<String, Object>> getByImoNumber(@PathVariable String imoNumber) {
        try {
            CommercialShipResponse response = commercialShipService.findByImoNumber(imoNumber);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navire récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Navire non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/type/{shipType}")
    @Operation(summary = "Lister par type", description = "Récupérer tous les navires d'un type spécifique (Pétrolier, Cargo, Porte-conteneurs, etc.)")
    public ResponseEntity<Map<String, Object>> listByType(@PathVariable String shipType) {
        try {
            List<CommercialShipResponse> responses = commercialShipService.findByShipType(shipType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des navires", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/status/{status}")
    @Operation(summary = "Lister par statut", description = "Récupérer tous les navires selon leur statut (En mer, Au port, En escorte, etc.)")
    public ResponseEntity<Map<String, Object>> listByStatus(@PathVariable String status) {
        try {
            List<CommercialShipResponse> responses = commercialShipService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des navires", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/flag/{flag}")
    @Operation(summary = "Lister par pavillon", description = "Récupérer tous les navires d'un pavillon spécifique")
    public ResponseEntity<Map<String, Object>> listByFlag(@PathVariable String flag) {
        try {
            List<CommercialShipResponse> responses = commercialShipService.findByFlag(flag);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des navires", null, e.getMessage()),
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
            List<CommercialShipResponse> responses = commercialShipService.findByArrivalDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des navires", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister tous les navires", description = "Récupérer la liste complète de tous les navires de commerce enregistrés")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<CommercialShipResponse> responses = commercialShipService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des navires", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un navire", description = "Supprimer définitivement un navire de commerce du système")
    public ResponseEntity<Map<String, Object>> deleteShip(@PathVariable UUID trackingId) {
        try {
            commercialShipService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navire supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression du navire", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
