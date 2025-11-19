package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.dto.HistoriesResponse;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.service.HistoriesService;
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
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoriesController {

    private final HistoriesService historiesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody HistoriesRequest request) {
        try {
            HistoriesResponse response = historiesService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'historique", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            HistoriesResponse response = historiesService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historique récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Historique non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgent(@PathVariable UUID agentTrackingId) {
        try {
            List<HistoriesResponse> responses = historiesService.findByAgent(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/entity/{entityTrackingId}")
    public ResponseEntity<Map<String, Object>> findByEntityTrackingId(@PathVariable UUID entityTrackingId) {
        try {
            List<HistoriesResponse> responses = historiesService.findByEntityTrackingId(entityTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/entity-name/{entityName}")
    public ResponseEntity<Map<String, Object>> findByEntityName(@PathVariable String entityName) {
        try {
            List<HistoriesResponse> responses = historiesService.findByEntityName(entityName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques récupérés avec succès pour l'entité " + entityName, responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/action-type/{actionType}")
    public ResponseEntity<Map<String, Object>> findByActionType(@PathVariable ActionType actionType) {
        try {
            List<HistoriesResponse> responses = historiesService.findByActionType(actionType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques récupérés avec succès pour l'action " + actionType, responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/period/{startDate}/{endDate}")
    public ResponseEntity<Map<String, Object>> findByCreateDateBetween(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<HistoriesResponse> responses = historiesService.findByCreateDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques récupérés avec succès pour la période", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<HistoriesResponse> responses = historiesService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Historiques récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des historiques", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
