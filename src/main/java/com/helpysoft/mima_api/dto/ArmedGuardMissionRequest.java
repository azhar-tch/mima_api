package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ArmedGuardMissionRequest {
    private UUID commercialShipTrackingId;
    private UUID securityAgencyTrackingId;
    private LocalDateTime embarkationDate;
    private LocalDateTime disembarkationDate;
    private String embarkationPort;
    private String disembarkationPort;
    private Integer personnelCount;
    private String patrolZone;
    private MissionStatus status;
    private String incidents;
    private String observations;
}
