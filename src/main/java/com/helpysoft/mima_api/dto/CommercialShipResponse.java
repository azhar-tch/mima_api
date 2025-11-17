package com.helpysoft.mima_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommercialShipResponse {
    private UUID trackingId;
    private String imoNumber;
    private String shipName;
    private String shipType;
    private String flag;
    private String mmsi;
    private String callSign;
    private Integer grossTonnage;
    private Integer deadWeight;
    private Double length;
    private Double width;
    private Double draft;
    private Integer yearBuilt;
    private String shipOwner;
    private String operator;
    private String lastPort;
    private String nextPort;
    private String cargoType;
    private LocalDateTime arrivalDate;
    private LocalDateTime departureDate;
    private String status;
    private String observations;
    private Boolean isActive;
    private LocalDateTime createDate;
}
