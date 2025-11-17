package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.DutiesRequest;
import com.helpysoft.mima_api.dto.DutiesResponse;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.service.DutiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/duties")
@RequiredArgsConstructor
public class DutiesController {

    private final DutiesService dutiesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody DutiesRequest request) {
        try {
            DutiesResponse response = dutiesService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Garde créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la garde", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody DutiesRequest request) {
        try {
            DutiesResponse response = dutiesService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Garde mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la garde", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            DutiesResponse response = dutiesService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Garde récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Garde non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgent(@PathVariable UUID agentTrackingId) {
        try {
            List<DutiesResponse> responses = dutiesService.findByAgent(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Gardes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des gardes", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> findByStatus(@PathVariable DutyStatus status) {
        try {
            List<DutiesResponse> responses = dutiesService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Gardes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des gardes", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<DutiesResponse> responses = dutiesService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Gardes récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des gardes", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            dutiesService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Garde supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la garde", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
