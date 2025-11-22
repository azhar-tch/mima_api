package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PALEntryExitRequest {

    private String commercialShipTrackingId; // UUID as String

    // Données d'entrée
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime entryDate;
    private String entryReason;
    private String anchorageZone;
    private String entryAuthorizationNumber;
    private String authorizingAuthority;

    // Données de sortie (optionnel, rempli en 2ème phase)
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime exitDate;
    private String exitReason;
    private String exitAuthorizationNumber;
    private String servicesProvided;

    private String incidents;
    private String observations;
}
