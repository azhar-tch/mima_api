package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ArmedGuardMissionRequest {
    private String missionNumber;
    private UUID commercialShipTrackingId;
    private UUID securityAgencyTrackingId;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime embarkationDate;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime disembarkationDate;
    private String embarkationPort;
    private String disembarkationPort;
    private Integer daysCount;
    private Integer personnelCount;
    private String patrolZone;
    private MissionStatus status;
    private String incidents;
    private String observations;
}
