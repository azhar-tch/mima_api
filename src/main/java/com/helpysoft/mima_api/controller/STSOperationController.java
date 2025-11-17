package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.STSOperationRequest;
import com.helpysoft.mima_api.dto.STSOperationResponse;
import com.helpysoft.mima_api.service.STSOperationService;
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
@RequestMapping("/api/sts-operations")
@RequiredArgsConstructor
@Tag(name = "STS Operations", description = "Gestion des opérations Ship-to-Ship (transbordement)")
public class STSOperationController {

    private final STSOperationService stsOperationService;

    @PostMapping("/create")
    @Operation(summary = "Enregistrer une opération STS", description = "Créer un nouvel enregistrement d'opération de transbordement")
    public ResponseEntity<Map<String, Object>> create(@RequestBody STSOperationRequest request) {
        try {
            STSOperationResponse response = stsOperationService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opération STS enregistrée avec succès", response, ""),
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
    @Operation(summary = "Mettre à jour une opération STS", description = "Modifier les informations d'une opération STS")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody STSOperationRequest request) {
        try {
            STSOperationResponse response = stsOperationService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opération STS mise à jour avec succès", response, ""),
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
    @Operation(summary = "Récupérer une opération STS", description = "Obtenir les détails d'une opération STS par son trackingId")
    public ResponseEntity<Map<String, Object>> getByTrackingId(@PathVariable UUID trackingId) {
        try {
            STSOperationResponse response = stsOperationService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opération STS récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Opération STS non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/operation-number/{operationNumber}")
    @Operation(summary = "Récupérer par numéro d'opération", description = "Obtenir une opération STS par son numéro")
    public ResponseEntity<Map<String, Object>> getByOperationNumber(@PathVariable String operationNumber) {
        try {
            STSOperationResponse response = stsOperationService.findByOperationNumber(operationNumber);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opération STS récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Opération STS non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/mother-vessel/{vesselTrackingId}")
    @Operation(summary = "Lister par navire donneur", description = "Récupérer toutes les opérations STS d'un navire donneur")
    public ResponseEntity<Map<String, Object>> listByMotherVessel(@PathVariable UUID vesselTrackingId) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findByMotherVessel(vesselTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations STS récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/receiving-vessel/{vesselTrackingId}")
    @Operation(summary = "Lister par navire receveur", description = "Récupérer toutes les opérations STS d'un navire receveur")
    public ResponseEntity<Map<String, Object>> listByReceivingVessel(@PathVariable UUID vesselTrackingId) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findByReceivingVessel(vesselTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations STS récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/supervising-vessel/{vesselTrackingId}")
    @Operation(summary = "Lister par moyen de supervision", description = "Récupérer toutes les opérations STS supervisées par un moyen naval")
    public ResponseEntity<Map<String, Object>> listBySupervisingVessel(@PathVariable UUID vesselTrackingId) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findBySupervisingVessel(vesselTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations STS récupérées avec succès", responses, ""),
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
    @Operation(summary = "Opérations en cours", description = "Liste des opérations STS actuellement en cours")
    public ResponseEntity<Map<String, Object>> listOngoingOperations() {
        try {
            List<STSOperationResponse> responses = stsOperationService.findOngoingOperations();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations en cours récupérées avec succès", responses, ""),
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
    @Operation(summary = "Lister par statut", description = "Récupérer les opérations selon leur statut (PLANIFIEE, EN_COURS, TERMINEE, ANNULEE)")
    public ResponseEntity<Map<String, Object>> listByStatus(@PathVariable String status) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/period")
    @Operation(summary = "Lister par période", description = "Récupérer les opérations débutées entre deux dates")
    public ResponseEntity<Map<String, Object>> listByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findByStartDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/cargo-type/{cargoType}")
    @Operation(summary = "Lister par type de cargaison", description = "Récupérer les opérations par type de cargaison transférée")
    public ResponseEntity<Map<String, Object>> listByCargoType(@PathVariable String cargoType) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findByCargoType(cargoType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations récupérées avec succès", responses, ""),
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
    @Operation(summary = "Opérations avec pollution", description = "Récupérer les opérations ayant causé une pollution")
    public ResponseEntity<Map<String, Object>> listOperationsWithPollution() {
        try {
            List<STSOperationResponse> responses = stsOperationService.findOperationsWithPollution();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations avec pollution récupérées avec succès", responses, ""),
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
    @Operation(summary = "Lister par zone maritime", description = "Récupérer les opérations par zone maritime")
    public ResponseEntity<Map<String, Object>> listByMaritimeZone(@PathVariable String maritimeZone) {
        try {
            List<STSOperationResponse> responses = stsOperationService.findByMaritimeZone(maritimeZone);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations récupérées avec succès", responses, ""),
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
    @Operation(summary = "Lister toutes les opérations", description = "Récupérer la liste complète de toutes les opérations STS")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<STSOperationResponse> responses = stsOperationService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opérations récupérées avec succès", responses, ""),
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
    @Operation(summary = "Supprimer une opération", description = "Supprimer définitivement un enregistrement d'opération STS")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            stsOperationService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Opération STS supprimée avec succès", null, ""),
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
