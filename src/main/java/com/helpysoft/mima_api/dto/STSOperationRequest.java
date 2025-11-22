package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class STSOperationRequest {

    private String motherVesselTrackingId; // Navire donneur - UUID as String
    private String receivingVesselTrackingId; // Navire receveur - UUID as String

    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime endDate; // Optionnel, rempli à la fin
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
    private String supervisingNavalVesselTrackingId; // UUID as String (optionnel)
    private String surveyCompany;
    private Boolean emergencyPlanEstablished;
    private String pollutionPreventionEquipment;

    // Incidents
    private String incidents;
    private Boolean pollutionOccurred;
    private String pollutionType;
    private String incidentMeasures;

    private String status; // PLANIFIEE, EN_COURS, TERMINEE, ANNULEE, SUSPENDUE
    private Boolean reportEstablished;
    private String reportReference;
    private Boolean compliantWithStandards;

    private String observations;
}
