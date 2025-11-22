package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.RulesRequest;
import com.helpysoft.mima_api.dto.RulesResponse;
import com.helpysoft.mima_api.service.RulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RulesController {

    private final RulesService rulesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody RulesRequest request) {
        try {
            RulesResponse response = rulesService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la règle", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody RulesRequest request) {
        try {
            RulesResponse response = rulesService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la règle", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            RulesResponse response = rulesService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Règle non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<RulesResponse> responses = rulesService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règles récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des règles", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            rulesService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la règle", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
