package com.helpysoft.mima_api.dto;

import lombok.Data;

@Data
public class ServicePositionRequest {
    private String positionName;
    private String positionType;
    private String location;
    private String unit;
    private String description;
}
