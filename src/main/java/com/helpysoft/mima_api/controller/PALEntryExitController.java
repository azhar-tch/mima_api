package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.PALEntryExitRequest;
import com.helpysoft.mima_api.dto.PALEntryExitResponse;
import com.helpysoft.mima_api.service.PALEntryExitService;
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
@RequestMapping("/api/pal-entry-exit")
@RequiredArgsConstructor
@Tag(name = "PAL Entry/Exit", description = "Gestion des entrées et sorties des navires au PAL (Port Autonome de Lomé)")
public class PALEntryExitController {

    private final PALEntryExitService palEntryExitService;

    @PostMapping("/create")
    @Operation(summary = "Enregistrer une entrée/sortie PAL", description = "Créer un nouvel enregistrement d'entrée ou de sortie du PAL")
    public ResponseEntity<Map<String, Object>> create(@RequestBody PALEntryExitRequest request) {
        try {
            PALEntryExitResponse response = palEntryExitService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Entrée/Sortie PAL enregistrée avec succès", response, ""),
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
    @Operation(summary = "Mettre à jour un enregistrement", description = "Modifier les informations d'entrée/sortie PAL d'un navire")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody PALEntryExitRequest request) {
        try {
            PALEntryExitResponse response = palEntryExitService.update(trackingId, request);
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
    @Operation(summary = "Récupérer un enregistrement", description = "Obtenir les détails d'un enregistrement d'entrée/sortie PAL par son trackingId")
    public ResponseEntity<Map<String, Object>> getByTrackingId(@PathVariable UUID trackingId) {
        try {
            PALEntryExitResponse response = palEntryExitService.findByTrackingId(trackingId);
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
    @Operation(summary = "Lister par navire", description = "Récupérer tous les enregistrements PAL d'un navire commercial")
    public ResponseEntity<Map<String, Object>> listByCommercialShip(@PathVariable UUID shipTrackingId) {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findByCommercialShip(shipTrackingId);
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

    @GetMapping("/list/currently-in-pal")
    @Operation(summary = "Navires actuellement au PAL", description = "Liste des navires actuellement présents au PAL")
    public ResponseEntity<Map<String, Object>> listCurrentlyInPAL() {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findShipsCurrentlyInPAL();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Navires au PAL récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/entries")
    @Operation(summary = "Lister par période d'entrée", description = "Récupérer les navires entrés au PAL entre deux dates")
    public ResponseEntity<Map<String, Object>> listByEntryPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findByEntryDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Entrées récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/exits")
    @Operation(summary = "Lister par période de sortie", description = "Récupérer les navires sortis du PAL entre deux dates")
    public ResponseEntity<Map<String, Object>> listByExitPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findByExitDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Sorties récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/anchorage-zone/{anchorageZone}")
    @Operation(summary = "Lister par zone de mouillage", description = "Récupérer les navires d'une zone de mouillage spécifique")
    public ResponseEntity<Map<String, Object>> listByAnchorageZone(@PathVariable String anchorageZone) {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findByAnchorageZone(anchorageZone);
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

    @GetMapping("/list/berth/{berthNumber}")
    @Operation(summary = "Lister par poste à quai", description = "Récupérer les navires d'un poste à quai spécifique")
    public ResponseEntity<Map<String, Object>> listByBerthNumber(@PathVariable String berthNumber) {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findByBerthNumber(berthNumber);
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
    @Operation(summary = "Lister tous les enregistrements", description = "Récupérer la liste complète de tous les enregistrements PAL")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<PALEntryExitResponse> responses = palEntryExitService.findAll();
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
    @Operation(summary = "Supprimer un enregistrement", description = "Supprimer définitivement un enregistrement d'entrée/sortie PAL")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            palEntryExitService.delete(trackingId);
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
