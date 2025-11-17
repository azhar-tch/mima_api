package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.PersonnelAllowanceRequest;
import com.helpysoft.mima_api.dto.PersonnelAllowanceResponse;
import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.service.PersonnelAllowanceService;
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
@RequestMapping("/api/personnel-allowances")
@RequiredArgsConstructor
@Tag(name = "Personnel Allowances", description = "Gestion des primes du personnel militaire par grade")
public class PersonnelAllowanceController {

    private final PersonnelAllowanceService personnelAllowanceService;

    @PostMapping("/create")
    @Operation(summary = "Créer une prime", description = "Enregistrer une nouvelle grille de primes pour un grade maritime")
    public ResponseEntity<Map<String, Object>> createAllowance(@RequestBody PersonnelAllowanceRequest request) {
        try {
            PersonnelAllowanceResponse response = personnelAllowanceService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Prime créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la prime", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour une prime", description = "Modifier les montants des primes pour un grade")
    public ResponseEntity<Map<String, Object>> updateAllowance(@PathVariable UUID trackingId, @RequestBody PersonnelAllowanceRequest request) {
        try {
            PersonnelAllowanceResponse response = personnelAllowanceService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Prime mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la prime", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer une prime", description = "Obtenir les détails d'une prime par son trackingId")
    public ResponseEntity<Map<String, Object>> getAllowance(@PathVariable UUID trackingId) {
        try {
            PersonnelAllowanceResponse response = personnelAllowanceService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Prime récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Prime non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/rank-code/{rankCode}")
    @Operation(summary = "Récupérer par code grade", description = "Obtenir les primes par code de grade (ex: CC, SM, MT1)")
    public ResponseEntity<Map<String, Object>> getByRankCode(@PathVariable String rankCode) {
        try {
            PersonnelAllowanceResponse response = personnelAllowanceService.findByRankCode(rankCode);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Prime récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Prime non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/maritime-rank/{maritimeRank}")
    @Operation(summary = "Récupérer par grade maritime", description = "Obtenir les primes par grade maritime")
    public ResponseEntity<Map<String, Object>> getByMaritimeRank(@PathVariable MaritimeRank maritimeRank) {
        try {
            PersonnelAllowanceResponse response = personnelAllowanceService.findByMaritimeRank(maritimeRank);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Prime récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Prime non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/active")
    @Operation(summary = "Lister primes actives", description = "Récupérer toutes les grilles de primes actives")
    public ResponseEntity<Map<String, Object>> listActive() {
        try {
            List<PersonnelAllowanceResponse> responses = personnelAllowanceService.findActiveAllowances();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Primes actives récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des primes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list/ordered")
    @Operation(summary = "Lister par montant", description = "Récupérer les primes classées par montant décroissant")
    public ResponseEntity<Map<String, Object>> listOrdered() {
        try {
            List<PersonnelAllowanceResponse> responses = personnelAllowanceService.findAllOrderByAllowance();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Primes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des primes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister toutes les primes", description = "Récupérer la liste complète de toutes les grilles de primes")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<PersonnelAllowanceResponse> responses = personnelAllowanceService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Primes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des primes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer une prime", description = "Supprimer définitivement une grille de primes du système")
    public ResponseEntity<Map<String, Object>> deleteAllowance(@PathVariable UUID trackingId) {
        try {
            personnelAllowanceService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Prime supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la prime", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
