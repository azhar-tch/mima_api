package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ManagementRulesRequest;
import com.helpysoft.mima_api.dto.ManagementRulesResponse;
import com.helpysoft.mima_api.service.ManagementRulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/management-rules")
@RequiredArgsConstructor
public class ManagementRulesController {

    private final ManagementRulesService managementRulesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ManagementRulesRequest request) {
        try {
            ManagementRulesResponse response = managementRulesService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle de gestion créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la règle de gestion", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody ManagementRulesRequest request) {
        try {
            ManagementRulesResponse response = managementRulesService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle de gestion mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la règle de gestion", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            ManagementRulesResponse response = managementRulesService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle de gestion récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Règle de gestion non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<ManagementRulesResponse> responses = managementRulesService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règles de gestion récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des règles de gestion", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            managementRulesService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Règle de gestion supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la règle de gestion", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
