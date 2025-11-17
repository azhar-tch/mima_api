package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AlertsRequest;
import com.helpysoft.mima_api.dto.AlertsResponse;
import com.helpysoft.mima_api.entity.AlertStatus;
import com.helpysoft.mima_api.service.AlertsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertsController {

    private final AlertsService alertsService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AlertsRequest request) {
        try {
            AlertsResponse response = alertsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'alerte", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AlertsRequest request) {
        try {
            AlertsResponse response = alertsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'alerte", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AlertsResponse response = alertsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Alerte non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> findByStatus(@PathVariable AlertStatus status) {
        try {
            List<AlertsResponse> responses = alertsService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgent(@PathVariable UUID agentTrackingId) {
        try {
            List<AlertsResponse> responses = alertsService.findByAgent(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes de l'agent récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AlertsResponse> responses = alertsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alertes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des alertes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            alertsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Alerte supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'alerte", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
