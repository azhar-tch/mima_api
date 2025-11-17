package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipIncidentResponse {

    private String trackingId;
    private Long id;

    // Navire
    private String commercialShipTrackingId;
    private String shipName;
    private String imoNumber;

    private LocalDateTime incidentDate;
    private String eventType;
    private String incidentType;
    private String severity;
    private String location;
    private Double latitude;
    private Double longitude;
    private String maritimeZone;
    private String description;
    private String causes;
    private String casualties;
    private String materialDamage;

    // Pollution
    private Boolean pollutionOccurred;
    private String pollutionType;

    private String respondingAgencies;
    private String assistingNavalVesselTrackingId;
    private String assistingVesselName;
    private String immediateMeasures;

    // Résolution
    private LocalDateTime resolutionDate;
    private Long resolutionDurationHours;
    private String status;
    private Boolean isResolved;
    private Boolean reportEstablished;
    private String reportReference;
    private String notifiedAuthorities;
    private Boolean investigationOngoing;
    private String recommendations;

    private String observations;
    private LocalDateTime createDate;
}
