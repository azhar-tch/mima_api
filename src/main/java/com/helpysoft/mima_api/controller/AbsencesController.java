package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AbsencesRequest;
import com.helpysoft.mima_api.dto.AbsencesResponse;
import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.service.AbsencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/absences")
@RequiredArgsConstructor
public class AbsencesController {

    private final AbsencesService absencesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAbsence(@RequestBody AbsencesRequest request) {
        try {
            AbsencesResponse response = absencesService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absence créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'absence", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> updateAbsence(@PathVariable UUID trackingId, @RequestBody AbsencesRequest request) {
        try {
            AbsencesResponse response = absencesService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absence mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'absence", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> getAbsence(@PathVariable UUID trackingId) {
        try {
            AbsencesResponse response = absencesService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absence récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Absence non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> listAbsencesByAgent(@PathVariable UUID agentTrackingId) {
        try {
            List<AbsencesResponse> responses = absencesService.findByAgent(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des absences", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PatchMapping("/update/status/{trackingId}")
    public ResponseEntity<Map<String, Object>> updateAbsenceStatus(
            @PathVariable UUID trackingId,
            @RequestParam AbsenceStatus status,
            @RequestParam(required = false) UUID validatedByTrackingId
    ) {
        try {
            AbsencesResponse response = absencesService.updateStatus(trackingId, status, validatedByTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Statut mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour du statut", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/status/{status}")
    public ResponseEntity<Map<String, Object>> listAbsencesByStatus(@PathVariable AbsenceStatus status) {
        try {
            List<AbsencesResponse> responses = absencesService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des absences", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listAbsences() {
        try {
            List<AbsencesResponse> responses = absencesService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absences récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des absences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAbsences(@RequestParam(required = false) String term) {
        try {
            List<AbsencesResponse> responses = absencesService.searchAbsences(term);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Recherche effectuée avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des absences", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> deleteAbsence(@PathVariable UUID trackingId) {
        try {
            absencesService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Absence supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'absence", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
