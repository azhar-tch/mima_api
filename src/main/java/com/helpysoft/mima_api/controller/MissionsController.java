package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.MissionsRequest;
import com.helpysoft.mima_api.dto.MissionsResponse;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.service.MissionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionsController {

    private final MissionsService missionsService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMission(@RequestBody MissionsRequest request) {
        try {
            MissionsResponse response = missionsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la mission", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> updateMission(@PathVariable UUID trackingId, @RequestBody MissionsRequest request) {
        try {
            MissionsResponse response = missionsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la mission", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> getMission(@PathVariable UUID trackingId) {
        try {
            MissionsResponse response = missionsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Mission non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/status/{status}")
    public ResponseEntity<Map<String, Object>> listMissionsByStatus(@PathVariable MissionStatus status) {
        try {
            List<MissionsResponse> responses = missionsService.findByStatus(status);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listMissions() {
        try {
            List<MissionsResponse> responses = missionsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Missions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des missions", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> deleteMission(@PathVariable UUID trackingId) {
        try {
            missionsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Mission supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la mission", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
