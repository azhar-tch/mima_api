package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.ShipStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommercialShipRequest {
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
    private ShipStatus status;
    private String observations;
    private Boolean isActive;
}
