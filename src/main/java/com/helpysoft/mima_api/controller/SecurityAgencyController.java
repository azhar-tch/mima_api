package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.SecurityAgencyRequest;
import com.helpysoft.mima_api.dto.SecurityAgencyResponse;
import com.helpysoft.mima_api.service.SecurityAgencyService;
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
@RequestMapping("/api/security-agencies")
@RequiredArgsConstructor
@Tag(name = "Security Agencies", description = "Gestion des agences de sécurité effectuant les demandes d'escorte et de garde armée")
public class SecurityAgencyController {

    private final SecurityAgencyService securityAgencyService;

    @PostMapping("/create")
    @Operation(summary = "Créer une agence de sécurité", description = "Enregistrer une nouvelle agence de sécurité")
    public ResponseEntity<Map<String, Object>> createAgency(@RequestBody SecurityAgencyRequest request) {
        try {
            SecurityAgencyResponse response = securityAgencyService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agence de sécurité créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'agence", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour une agence", description = "Modifier les informations d'une agence de sécurité existante")
    public ResponseEntity<Map<String, Object>> updateAgency(@PathVariable UUID trackingId, @RequestBody SecurityAgencyRequest request) {
        try {
            SecurityAgencyResponse response = securityAgencyService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agence mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'agence", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer une agence", description = "Obtenir les détails complets d'une agence par son trackingId")
    public ResponseEntity<Map<String, Object>> getAgency(@PathVariable UUID trackingId) {
        try {
            SecurityAgencyResponse response = securityAgencyService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agence récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Agence non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/agency-number/{agencyNumber}")
    @Operation(summary = "Récupérer par numéro d'agence", description = "Obtenir une agence par son numéro d'identification")
    public ResponseEntity<Map<String, Object>> getByAgencyNumber(@PathVariable String agencyNumber) {
        try {
            SecurityAgencyResponse response = securityAgencyService.findByAgencyNumber(agencyNumber);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agence récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Agence non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/name/{agencyName}")
    @Operation(summary = "Rechercher par nom", description = "Rechercher des agences par leur nom (recherche partielle)")
    public ResponseEntity<Map<String, Object>> searchByName(@PathVariable String agencyName) {
        try {
            List<SecurityAgencyResponse> responses = securityAgencyService.findByAgencyName(agencyName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des agences", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/active")
    @Operation(summary = "Lister agences actives", description = "Récupérer toutes les agences de sécurité actives")
    public ResponseEntity<Map<String, Object>> listActive() {
        try {
            List<SecurityAgencyResponse> responses = securityAgencyService.findActiveAgencies();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agences actives récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des agences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list/top-escorts")
    @Operation(summary = "Top agences par escortes", description = "Récupérer les agences classées par nombre d'escortes demandées")
    public ResponseEntity<Map<String, Object>> listTopByEscorts() {
        try {
            List<SecurityAgencyResponse> responses = securityAgencyService.findTopAgenciesByEscorts();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Top agences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des agences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list/top-armed-guards")
    @Operation(summary = "Top agences par gardes armées", description = "Récupérer les agences classées par nombre de gardes armées demandées")
    public ResponseEntity<Map<String, Object>> listTopByArmedGuards() {
        try {
            List<SecurityAgencyResponse> responses = securityAgencyService.findTopAgenciesByArmedGuards();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Top agences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des agences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister toutes les agences", description = "Récupérer la liste complète de toutes les agences de sécurité enregistrées")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<SecurityAgencyResponse> responses = securityAgencyService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des agences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des agences", description = "Rechercher des agences par nom, numéro, contact, ville, etc.")
    public ResponseEntity<Map<String, Object>> searchAgencies(@RequestParam(required = false) String term) {
        try {
            List<SecurityAgencyResponse> responses = securityAgencyService.searchSecurityAgencies(term);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Recherche effectuée avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des agences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer une agence", description = "Supprimer définitivement une agence de sécurité du système")
    public ResponseEntity<Map<String, Object>> deleteAgency(@PathVariable UUID trackingId) {
        try {
            securityAgencyService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Agence supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'agence", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
