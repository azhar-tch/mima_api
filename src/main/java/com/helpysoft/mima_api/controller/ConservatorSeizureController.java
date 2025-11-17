package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ConservatorSeizureRequest;
import com.helpysoft.mima_api.dto.ConservatorSeizureResponse;
import com.helpysoft.mima_api.service.ConservatorSeizureService;
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
@RequestMapping("/api/conservator-seizures")
@RequiredArgsConstructor
@Tag(name = "Conservator Seizures", description = "Gestion des saisies conservatoires des navires")
public class ConservatorSeizureController {

    private final ConservatorSeizureService conservatorSeizureService;

    @PostMapping("/create")
    @Operation(summary = "Enregistrer une saisie", description = "Créer un nouvel enregistrement de saisie conservatoire")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ConservatorSeizureRequest request) {
        try {
            ConservatorSeizureResponse response = conservatorSeizureService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisie conservatoire enregistrée avec succès", response, ""),
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
    @Operation(summary = "Mettre à jour une saisie", description = "Modifier les informations d'une saisie conservatoire")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody ConservatorSeizureRequest request) {
        try {
            ConservatorSeizureResponse response = conservatorSeizureService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisie mise à jour avec succès", response, ""),
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
    @Operation(summary = "Récupérer une saisie", description = "Obtenir les détails d'une saisie conservatoire par son trackingId")
    public ResponseEntity<Map<String, Object>> getByTrackingId(@PathVariable UUID trackingId) {
        try {
            ConservatorSeizureResponse response = conservatorSeizureService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisie récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Saisie non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/commercial-ship/{shipTrackingId}")
    @Operation(summary = "Lister par navire", description = "Récupérer toutes les saisies d'un navire commercial")
    public ResponseEntity<Map<String, Object>> listByCommercialShip(@PathVariable UUID shipTrackingId) {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findByCommercialShip(shipTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/active")
    @Operation(summary = "Saisies actives", description = "Liste des saisies conservatoires actuellement en cours")
    public ResponseEntity<Map<String, Object>> listActiveSeizures() {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findActiveSeizures();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies actives récupérées avec succès", responses, ""),
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
    @Operation(summary = "Lister par statut", description = "Récupérer les saisies selon leur statut (EN_COURS, LEVEE, ANNULEE)")
    public ResponseEntity<Map<String, Object>> listByStatus(@PathVariable String status) {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies récupérées avec succès", responses, ""),
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
    @Operation(summary = "Lister par période", description = "Récupérer les saisies effectuées entre deux dates")
    public ResponseEntity<Map<String, Object>> listByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findBySeizureDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/authority/{seizingAuthority}")
    @Operation(summary = "Lister par autorité", description = "Récupérer les saisies par autorité saisissante")
    public ResponseEntity<Map<String, Object>> listBySeizingAuthority(@PathVariable String seizingAuthority) {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findBySeizingAuthority(seizingAuthority);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/type/{seizureType}")
    @Operation(summary = "Lister par type", description = "Récupérer les saisies par type de saisie")
    public ResponseEntity<Map<String, Object>> listBySeizureType(@PathVariable String seizureType) {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findBySeizureType(seizureType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies récupérées avec succès", responses, ""),
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
    @Operation(summary = "Lister toutes les saisies", description = "Récupérer la liste complète de toutes les saisies conservatoires")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<ConservatorSeizureResponse> responses = conservatorSeizureService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisies récupérées avec succès", responses, ""),
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
    @Operation(summary = "Supprimer une saisie", description = "Supprimer définitivement un enregistrement de saisie conservatoire")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            conservatorSeizureService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Saisie supprimée avec succès", null, ""),
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
