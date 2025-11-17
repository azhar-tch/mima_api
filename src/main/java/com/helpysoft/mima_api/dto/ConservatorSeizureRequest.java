package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConservatorSeizureRequest {

    private String commercialShipTrackingId; // UUID as String

    // Données de saisie
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
    private LocalDateTime releaseDate;
    private String releaseReason;
    private String releaseOrderNumber;
    private Double amountPaid;
    private String relatedDocuments;

    private String observations;
}
