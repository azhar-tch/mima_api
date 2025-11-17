package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipProvisioningRequest {

    private String commercialShipTrackingId; // UUID as String

    private LocalDateTime provisioningDate;
    private String provisioningType;
    private String supplierName;
    private String supplyVesselName;
    private String supplyVesselImo;
    private String productType;
    private Double quantity;
    private String unit;
    private Double amount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String provisioningPoint;

    // Retard (intégré de Table 21)
    private Boolean hasDelay;
    private Double delayDurationHours;
    private String delayReason;
    private Double delayPenalty;
    private String correctiveActions;

    private String incidents;
    private String observations;
}
