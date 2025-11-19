package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.NavalVesselRequest;
import com.helpysoft.mima_api.dto.NavalVesselResponse;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import com.helpysoft.mima_api.service.NavalVesselService;
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
@RequestMapping("/api/naval-vessels")
@RequiredArgsConstructor
@Tag(name = "Naval Vessels", description = "Gestion des moyens navals de la marine nationale (Patrouilleurs, Vedettes, Embarcations)")
public class NavalVesselController {

    private final NavalVesselService navalVesselService;

    @PostMapping("/create")
    @Operation(summary = "Créer un moyen naval", description = "Enregistrer un nouveau moyen naval (PHM, VDT, EMB, etc.)")
    public ResponseEntity<Map<String, Object>> createVessel(@RequestBody NavalVesselRequest request) {
        try {
            NavalVesselResponse response = navalVesselService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyen naval créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création du moyen naval", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour un moyen naval", description = "Modifier les informations d'un moyen naval existant")
    public ResponseEntity<Map<String, Object>> updateVessel(@PathVariable UUID trackingId, @RequestBody NavalVesselRequest request) {
        try {
            NavalVesselResponse response = navalVesselService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyen naval mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour du moyen naval", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer un moyen naval", description = "Obtenir les détails complets d'un moyen naval par son trackingId")
    public ResponseEntity<Map<String, Object>> getVessel(@PathVariable UUID trackingId) {
        try {
            NavalVesselResponse response = navalVesselService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyen naval récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Moyen naval non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/vessel-number/{vesselNumber}")
    @Operation(summary = "Récupérer par numéro de navire", description = "Obtenir un moyen naval par son numéro d'identification")
    public ResponseEntity<Map<String, Object>> getByVesselNumber(@PathVariable String vesselNumber) {
        try {
            NavalVesselResponse response = navalVesselService.findByVesselNumber(vesselNumber);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyen naval récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Moyen naval non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/type/{vesselType}")
    @Operation(summary = "Lister par type", description = "Récupérer tous les moyens navals d'un type spécifique (PHM, VDT, EMB, etc.)")
    public ResponseEntity<Map<String, Object>> listByType(@PathVariable NavalVesselType vesselType) {
        try {
            List<NavalVesselResponse> responses = navalVesselService.findByVesselType(vesselType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyens navals récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des moyens navals", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/status/{status}")
    @Operation(summary = "Lister par statut opérationnel", description = "Récupérer tous les moyens navals selon leur statut (OPERATIONAL, MAINTENANCE, RETIRED)")
    public ResponseEntity<Map<String, Object>> listByStatus(@PathVariable NavalVesselStatus status) {
        try {
            List<NavalVesselResponse> responses = navalVesselService.findByOperationalStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyens navals récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des moyens navals", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/available")
    @Operation(summary = "Lister moyens disponibles", description = "Récupérer tous les moyens navals opérationnels et actifs")
    public ResponseEntity<Map<String, Object>> listAvailable() {
        try {
            List<NavalVesselResponse> responses = navalVesselService.findAvailableVessels();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyens navals disponibles récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des moyens navals", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list/patrol")
    @Operation(summary = "Lister moyens de patrouille", description = "Récupérer tous les moyens navals aptes à la patrouille (PHM, VDT)")
    public ResponseEntity<Map<String, Object>> listPatrolVessels() {
        try {
            List<NavalVesselResponse> responses = navalVesselService.findPatrolVessels();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyens de patrouille récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des moyens de patrouille", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister tous les moyens navals", description = "Récupérer la liste complète de tous les moyens navals enregistrés")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<NavalVesselResponse> responses = navalVesselService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyens navals récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des moyens navals", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des moyens navals", description = "Rechercher des moyens navals par nom, numéro, port d'attache, etc.")
    public ResponseEntity<Map<String, Object>> searchVessels(@RequestParam(required = false) String term) {
        try {
            List<NavalVesselResponse> responses = navalVesselService.searchNavalVessels(term);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Recherche effectuée avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des moyens navals", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un moyen naval", description = "Supprimer définitivement un moyen naval du système")
    public ResponseEntity<Map<String, Object>> deleteVessel(@PathVariable UUID trackingId) {
        try {
            navalVesselService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Moyen naval supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression du moyen naval", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
