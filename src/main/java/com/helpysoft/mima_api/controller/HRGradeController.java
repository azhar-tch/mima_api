package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.HRGradeRequest;
import com.helpysoft.mima_api.dto.HRGradeResponse;
import com.helpysoft.mima_api.service.HRGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/hr-grades")
@RequiredArgsConstructor
public class HRGradeController {

    private final HRGradeService hrGradeService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody HRGradeRequest request) {
        try {
            HRGradeResponse response = hrGradeService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grade créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création du grade", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody HRGradeRequest request) {
        try {
            HRGradeResponse response = hrGradeService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grade mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour du grade", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            HRGradeResponse response = hrGradeService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grade récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Grade non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{gradeName}")
    public ResponseEntity<Map<String, Object>> findByGradeName(@PathVariable String gradeName) {
        try {
            HRGradeResponse response = hrGradeService.findByGradeName(gradeName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grade récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Grade non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/ordered")
    public ResponseEntity<Map<String, Object>> findAllOrderedByHierarchy() {
        try {
            List<HRGradeResponse> responses = hrGradeService.findAllOrderedByHierarchy();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grades récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des grades", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<HRGradeResponse> responses = hrGradeService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grades récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des grades", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            hrGradeService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Grade supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression du grade", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
