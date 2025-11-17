package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipIncidentRequest {

    private String commercialShipTrackingId; // UUID as String

    private LocalDateTime incidentDate;
    private String eventType; // INCIDENT ou ASSISTANCE
    private String incidentType;
    private String severity; // FAIBLE, MOYENNE, GRAVE, CRITIQUE
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
    private String assistingNavalVesselTrackingId; // UUID as String (optionnel)
    private String immediateMeasures;

    // Résolution (optionnel)
    private LocalDateTime resolutionDate;
    private String status; // EN_COURS, RESOLU, EN_INVESTIGATION
    private Boolean reportEstablished;
    private String reportReference;
    private String notifiedAuthorities;
    private Boolean investigationOngoing;
    private String recommendations;

    private String observations;
}
