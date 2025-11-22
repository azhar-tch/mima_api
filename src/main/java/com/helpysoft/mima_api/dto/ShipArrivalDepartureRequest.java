package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipArrivalDepartureRequest {

    private String commercialShipTrackingId; // UUID as String

    // Données d'arrivée
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime arrivalDate;
    private String portOfOrigin;
    private String cargoTypeArrival;
    private Double cargoQuantityArrival;
    private Integer passengersArrival;
    private Integer crewCount;
    private String captainName;
    private String shippingAgent;
    private String berthingPosition;

    // Données de départ (optionnel, rempli en 2ème phase)
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime departureDate;
    private String portOfDestination;
    private String cargoTypeDeparture;
    private Double cargoQuantityDeparture;
    private Integer passengersDeparture;
    private String servicesProvided;
    private Double portDues;

    private String incidents;
    private String observations;
}
