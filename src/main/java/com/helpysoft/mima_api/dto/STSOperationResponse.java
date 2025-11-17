package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class STSOperationResponse {

    private String trackingId;
    private Long id;
    private String operationNumber;

    // Navires
    private String motherVesselTrackingId;
    private String motherVesselName;
    private String motherVesselImo;
    private String receivingVesselTrackingId;
    private String receivingVesselName;
    private String receivingVesselImo;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double operationDurationHours;
    private String cargoType;
    private Double quantityTransferred;
    private String unit;
    private String location;
    private Double latitude;
    private Double longitude;
    private String maritimeZone;

    // Conditions
    private String weatherConditions;
    private Integer seaState;

    // Autorisation
    private String stsOperator;
    private String authorizationNumber;
    private String authorizingAuthority;

    // Supervision
    private String supervisingNavalVesselTrackingId;
    private String supervisingVesselName;
    private String surveyCompany;
    private Boolean emergencyPlanEstablished;
    private String pollutionPreventionEquipment;

    // Incidents
    private String incidents;
    private Boolean pollutionOccurred;
    private String pollutionType;
    private String incidentMeasures;

    private String status;
    private Boolean isCompleted;
    private Boolean reportEstablished;
    private String reportReference;
    private Boolean compliantWithStandards;

    private String observations;
    private LocalDateTime createDate;
}
