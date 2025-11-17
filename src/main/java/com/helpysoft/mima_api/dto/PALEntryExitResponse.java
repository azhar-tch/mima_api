package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PALEntryExitResponse {

    private String trackingId;
    private Long id;

    // Navire
    private String commercialShipTrackingId;
    private String shipName;
    private String imoNumber;

    // Données d'entrée
    private LocalDateTime entryDate;
    private String entryReason;
    private String anchorageZone;
    private String entryAuthorizationNumber;
    private String authorizingAuthority;

    // Données de sortie
    private LocalDateTime exitDate;
    private String exitReason;
    private String exitAuthorizationNumber;
    private Long stayDurationHours;
    private Long stayDurationDays;
    private String servicesProvided;

    private String incidents;
    private String observations;
    private LocalDateTime createDate;
}
