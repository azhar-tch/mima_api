package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ServicePositionRequest {
    private String positionName;
    private String positionType;
    private String location;
    private UUID unitTrackingId;
    private String description;
}
