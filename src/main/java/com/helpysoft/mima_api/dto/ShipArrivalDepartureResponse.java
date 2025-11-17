package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShipArrivalDepartureResponse {

    private String trackingId;
    private Long id;

    // Navire
    private String commercialShipTrackingId;
    private String shipName;
    private String imoNumber;

    // Données d'arrivée
    private LocalDateTime arrivalDate;
    private String portOfOrigin;
    private String cargoTypeArrival;
    private Double cargoQuantityArrival;
    private Integer passengersArrival;
    private Integer crewCount;
    private String captainName;
    private String shippingAgent;
    private String berthingPosition;

    // Données de départ
    private LocalDateTime departureDate;
    private String portOfDestination;
    private String cargoTypeDeparture;
    private Double cargoQuantityDeparture;
    private Integer passengersDeparture;
    private Long stayDurationHours;
    private Long stayDurationDays;
    private String servicesProvided;
    private Double portDues;

    private String incidents;
    private String observations;
    private LocalDateTime createDate;
}
