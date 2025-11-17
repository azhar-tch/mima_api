package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ServicePositionRequest;
import com.helpysoft.mima_api.dto.ServicePositionResponse;
import com.helpysoft.mima_api.service.ServicePositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-positions")
@RequiredArgsConstructor
public class ServicePositionController {

    private final ServicePositionService servicePositionService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ServicePositionRequest request) {
        try {
            ServicePositionResponse response = servicePositionService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectation créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'affectation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody ServicePositionRequest request) {
        try {
            ServicePositionResponse response = servicePositionService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectation mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'affectation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            ServicePositionResponse response = servicePositionService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectation récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Affectation non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{positionName}")
    public ResponseEntity<Map<String, Object>> findByPositionName(@PathVariable String positionName) {
        try {
            ServicePositionResponse response = servicePositionService.findByPositionName(positionName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectation récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Affectation non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/type/{positionType}")
    public ResponseEntity<Map<String, Object>> findByPositionType(@PathVariable String positionType) {
        try {
            List<ServicePositionResponse> responses = servicePositionService.findByPositionType(positionType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des affectations", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<Map<String, Object>> findByLocation(@PathVariable String location) {
        try {
            List<ServicePositionResponse> responses = servicePositionService.findByLocation(location);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des affectations", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<ServicePositionResponse> responses = servicePositionService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des affectations", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            servicePositionService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Affectation supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'affectation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
