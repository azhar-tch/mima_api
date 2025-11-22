package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipProvisioningRequest {

    private String commercialShipTrackingId; // UUID as String

    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime provisioningDate;
    private String provisioningType;
    private String supplierName;
    private String supplyVesselName;
    private String supplyVesselImo;
    private String productType;
    private Double quantity;
    private String unit;
    private Double amount;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
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
