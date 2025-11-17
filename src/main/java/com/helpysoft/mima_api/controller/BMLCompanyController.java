package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.BMLCompanyRequest;
import com.helpysoft.mima_api.dto.BMLCompanyResponse;
import com.helpysoft.mima_api.service.BMLCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/bml-companies")
@RequiredArgsConstructor
public class BMLCompanyController {

    private final BMLCompanyService bmlCompanyService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody BMLCompanyRequest request) {
        try {
            BMLCompanyResponse response = bmlCompanyService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnie créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la compagnie", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody BMLCompanyRequest request) {
        try {
            BMLCompanyResponse response = bmlCompanyService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnie mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la compagnie", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            BMLCompanyResponse response = bmlCompanyService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnie récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Compagnie non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/name/{companyName}")
    public ResponseEntity<Map<String, Object>> findByCompanyName(@PathVariable String companyName) {
        try {
            BMLCompanyResponse response = bmlCompanyService.findByCompanyName(companyName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnie récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Compagnie non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<BMLCompanyResponse> responses = bmlCompanyService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnies récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des compagnies", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            bmlCompanyService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Compagnie supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la compagnie", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
