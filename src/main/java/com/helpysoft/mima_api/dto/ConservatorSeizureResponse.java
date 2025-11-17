package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConservatorSeizureResponse {

    private String trackingId;
    private Long id;

    // Navire
    private String commercialShipTrackingId;
    private String shipName;
    private String imoNumber;

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

    // Données de levée
    private LocalDateTime releaseDate;
    private String releaseReason;
    private String releaseOrderNumber;
    private Double amountPaid;
    private Long seizureDurationHours;
    private Long seizureDurationDays;
    private String status;
    private String relatedDocuments;

    private String observations;
    private LocalDateTime createDate;
}
