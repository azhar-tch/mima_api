package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.TrainingRequest;
import com.helpysoft.mima_api.dto.TrainingResponse;
import com.helpysoft.mima_api.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody TrainingRequest request) {
        try {
            TrainingResponse response = trainingService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formation créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la formation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody TrainingRequest request) {
        try {
            TrainingResponse response = trainingService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formation mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la formation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            TrainingResponse response = trainingService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formation récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Formation non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{trainingName}")
    public ResponseEntity<Map<String, Object>> findByTrainingName(@PathVariable String trainingName) {
        try {
            TrainingResponse response = trainingService.findByTrainingName(trainingName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formation récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Formation non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/type/{trainingType}")
    public ResponseEntity<Map<String, Object>> findByTrainingType(@PathVariable String trainingType) {
        try {
            List<TrainingResponse> responses = trainingService.findByTrainingType(trainingType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des formations", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<TrainingResponse> responses = trainingService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des formations", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            trainingService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Formation supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la formation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
