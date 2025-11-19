package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.UnitsRequest;
import com.helpysoft.mima_api.dto.UnitsResponse;
import com.helpysoft.mima_api.service.UnitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitsController {

    private final UnitsService unitsService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUnit(@RequestBody UnitsRequest request) {
        try {
            UnitsResponse response = unitsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Unité créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'unité", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> updateUnit(@PathVariable UUID trackingId, @RequestBody UnitsRequest request) {
        try {
            UnitsResponse response = unitsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Unité mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'unité", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> getUnit(@PathVariable UUID trackingId) {
        try {
            UnitsResponse response = unitsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Unité récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Unité non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listUnits() {
        try {
            List<UnitsResponse> responses = unitsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Unités récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des unités", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUnits(@RequestParam(required = false) String term) {
        try {
            List<UnitsResponse> responses = unitsService.searchUnits(term);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Recherche effectuée avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la recherche des unités", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            unitsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Unité supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'unité", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
