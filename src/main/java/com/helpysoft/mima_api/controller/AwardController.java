package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.AwardRequest;
import com.helpysoft.mima_api.dto.AwardResponse;
import com.helpysoft.mima_api.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody AwardRequest request) {
        try {
            AwardResponse response = awardService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinction créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la distinction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody AwardRequest request) {
        try {
            AwardResponse response = awardService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinction mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la distinction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            AwardResponse response = awardService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinction récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Distinction non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{awardName}")
    public ResponseEntity<Map<String, Object>> findByAwardName(@PathVariable String awardName) {
        try {
            AwardResponse response = awardService.findByAwardName(awardName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinction récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Distinction non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/type/{awardType}")
    public ResponseEntity<Map<String, Object>> findByAwardType(@PathVariable String awardType) {
        try {
            List<AwardResponse> responses = awardService.findByAwardType(awardType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinctions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des distinctions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<AwardResponse> responses = awardService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinctions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des distinctions", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            awardService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Distinction supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la distinction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
