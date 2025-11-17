package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.OtherPositionRequest;
import com.helpysoft.mima_api.dto.OtherPositionResponse;
import com.helpysoft.mima_api.service.OtherPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/other-positions")
@RequiredArgsConstructor
public class OtherPositionController {

    private final OtherPositionService otherPositionService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody OtherPositionRequest request) {
        try {
            OtherPositionResponse response = otherPositionService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autre position créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'autre position", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody OtherPositionRequest request) {
        try {
            OtherPositionResponse response = otherPositionService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autre position mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'autre position", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            OtherPositionResponse response = otherPositionService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autre position récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Autre position non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{positionName}")
    public ResponseEntity<Map<String, Object>> findByPositionName(@PathVariable String positionName) {
        try {
            OtherPositionResponse response = otherPositionService.findByPositionName(positionName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autre position récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Autre position non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/type/{positionType}")
    public ResponseEntity<Map<String, Object>> findByPositionType(@PathVariable String positionType) {
        try {
            List<OtherPositionResponse> responses = otherPositionService.findByPositionType(positionType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autres positions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des autres positions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<OtherPositionResponse> responses = otherPositionService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autres positions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des autres positions", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            otherPositionService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Autre position supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'autre position", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
