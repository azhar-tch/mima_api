package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.EscortType;
import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EscortMissionRequest {
    private String missionNumber;
    private UUID commercialShipTrackingId;
    private UUID securityAgencyTrackingId;
    private UUID navalVesselTrackingId;
    private UUID commanderTrackingId;
    private String commanderRank;
    private UUID secondaryVesselTrackingId;
    private String vedettes;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EscortType escortType;
    private String departurePoint;
    private String arrivalPoint;
    private Double distance;
    private String escortZone;
    private MissionStatus status;
    private String incidents;
    private String observations;
}
