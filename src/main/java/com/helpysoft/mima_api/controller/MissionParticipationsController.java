package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.MissionParticipationsRequest;
import com.helpysoft.mima_api.dto.MissionParticipationsResponse;
import com.helpysoft.mima_api.service.MissionParticipationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/mission-participations")
@RequiredArgsConstructor
public class MissionParticipationsController {

    private final MissionParticipationsService missionParticipationsService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody MissionParticipationsRequest request) {
        try {
            MissionParticipationsResponse response = missionParticipationsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participation créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la participation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody MissionParticipationsRequest request) {
        try {
            MissionParticipationsResponse response = missionParticipationsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participation mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la participation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            MissionParticipationsResponse response = missionParticipationsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participation récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Participation non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/mission/{missionTrackingId}")
    public ResponseEntity<Map<String, Object>> findByMission(@PathVariable UUID missionTrackingId) {
        try {
            List<MissionParticipationsResponse> responses = missionParticipationsService.findByMission(missionTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des participations", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/agent/{agentTrackingId}")
    public ResponseEntity<Map<String, Object>> findByAgent(@PathVariable UUID agentTrackingId) {
        try {
            List<MissionParticipationsResponse> responses = missionParticipationsService.findByAgent(agentTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des participations", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<MissionParticipationsResponse> responses = missionParticipationsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participations récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des participations", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            missionParticipationsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Participation supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la participation", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
