package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.ShipProvisioningRequest;
import com.helpysoft.mima_api.dto.ShipProvisioningResponse;
import com.helpysoft.mima_api.service.ShipProvisioningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ship-provisioning")
@RequiredArgsConstructor
@Tag(name = "Ship Provisioning", description = "Gestion de l'avitaillement des navires")
public class ShipProvisioningController {

    private final ShipProvisioningService shipProvisioningService;

    @PostMapping("/create")
    @Operation(summary = "Enregistrer un avitaillement", description = "Créer un nouvel enregistrement d'avitaillement de navire")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ShipProvisioningRequest request) {
        try {
            ShipProvisioningResponse response = shipProvisioningService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillement enregistré avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de l'enregistrement", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    @Operation(summary = "Mettre à jour un avitaillement", description = "Modifier les informations d'un avitaillement")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody ShipProvisioningRequest request) {
        try {
            ShipProvisioningResponse response = shipProvisioningService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillement mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    @Operation(summary = "Récupérer un avitaillement", description = "Obtenir les détails d'un avitaillement par son trackingId")
    public ResponseEntity<Map<String, Object>> getByTrackingId(@PathVariable UUID trackingId) {
        try {
            ShipProvisioningResponse response = shipProvisioningService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillement récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Avitaillement non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list/commercial-ship/{shipTrackingId}")
    @Operation(summary = "Lister par navire", description = "Récupérer tous les avitaillements d'un navire commercial")
    public ResponseEntity<Map<String, Object>> listByCommercialShip(@PathVariable UUID shipTrackingId) {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findByCommercialShip(shipTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/period/{startDate}/{endDate}")
    @Operation(summary = "Lister par période", description = "Récupérer les avitaillements effectués entre deux dates")
    public ResponseEntity<Map<String, Object>> listByPeriod(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findByProvisioningDateBetween(startDate, endDate);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/type/{provisioningType}")
    @Operation(summary = "Lister par type d'avitaillement", description = "Récupérer les avitaillements par type (FUEL, WATER, FOOD, SPARE_PARTS, etc.)")
    public ResponseEntity<Map<String, Object>> listByProvisioningType(@PathVariable String provisioningType) {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findByProvisioningType(provisioningType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/supplier/{supplierName}")
    @Operation(summary = "Lister par fournisseur", description = "Récupérer les avitaillements par fournisseur")
    public ResponseEntity<Map<String, Object>> listBySupplier(@PathVariable String supplierName) {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findBySupplier(supplierName);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/with-delay")
    @Operation(summary = "Lister les retards", description = "Récupérer les avitaillements qui ont subi des retards")
    public ResponseEntity<Map<String, Object>> listWithDelay() {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findProvisioningsWithDelay();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements avec retard récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list/product-type/{productType}")
    @Operation(summary = "Lister par type de produit", description = "Récupérer les avitaillements par type de produit")
    public ResponseEntity<Map<String, Object>> listByProductType(@PathVariable String productType) {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findByProductType(productType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Lister tous les avitaillements", description = "Récupérer la liste complète de tous les avitaillements")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<ShipProvisioningResponse> responses = shipProvisioningService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillements récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    @Operation(summary = "Supprimer un avitaillement", description = "Supprimer définitivement un enregistrement d'avitaillement")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            shipProvisioningService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Avitaillement supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
