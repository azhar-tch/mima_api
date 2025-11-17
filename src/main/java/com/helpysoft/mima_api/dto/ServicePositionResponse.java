package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ServicePositionResponse {
    private UUID trackingId;
    private String positionName;
    private String positionType;
    private String location;
    private String unit;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
