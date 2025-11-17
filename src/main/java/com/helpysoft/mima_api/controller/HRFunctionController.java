package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.HRFunctionRequest;
import com.helpysoft.mima_api.dto.HRFunctionResponse;
import com.helpysoft.mima_api.service.HRFunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/hr-functions")
@RequiredArgsConstructor
public class HRFunctionController {

    private final HRFunctionService hrFunctionService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody HRFunctionRequest request) {
        try {
            HRFunctionResponse response = hrFunctionService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonction créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la fonction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody HRFunctionRequest request) {
        try {
            HRFunctionResponse response = hrFunctionService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonction mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la fonction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            HRFunctionResponse response = hrFunctionService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonction récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Fonction non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{functionName}")
    public ResponseEntity<Map<String, Object>> findByFunctionName(@PathVariable String functionName) {
        try {
            HRFunctionResponse response = hrFunctionService.findByFunctionName(functionName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonction récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Fonction non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/type/{functionType}")
    public ResponseEntity<Map<String, Object>> findByFunctionType(@PathVariable String functionType) {
        try {
            List<HRFunctionResponse> responses = hrFunctionService.findByFunctionType(functionType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonctions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des fonctions", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<HRFunctionResponse> responses = hrFunctionService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonctions récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des fonctions", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            hrFunctionService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Fonction supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la fonction", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
