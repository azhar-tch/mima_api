package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipProvisioningResponse {

    private String trackingId;
    private Long id;

    // Navire
    private String commercialShipTrackingId;
    private String shipName;
    private String imoNumber;

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
    private Double operationDurationHours;
    private String provisioningPoint;

    // Retard
    private Boolean hasDelay;
    private Double delayDurationHours;
    private String delayReason;
    private Double delayPenalty;
    private String correctiveActions;
    private Boolean isOnTime;

    private String incidents;
    private String observations;
    private LocalDateTime createDate;
}
