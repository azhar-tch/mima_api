package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConservatorSeizureRequest {

    private String commercialShipTrackingId; // UUID as String

    // Données de saisie
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime seizureDate;
    private String seizingAuthority;
    private String seizureOrderNumber;
    private String seizureReason;
    private String seizureType;
    private Double claimAmount;
    private String seizureLocation;
    private String creditorName;
    private String creditorLegalRepresentative;
    private String bailiffName;
    private String shipGuardian;

    // Données de levée (optionnel, rempli en 2ème phase)
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime releaseDate;
    private String releaseReason;
    private String releaseOrderNumber;
    private Double amountPaid;
    private String relatedDocuments;

    private String observations;
}
