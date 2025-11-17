package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.EscortType;
import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscortMissionResponse {
    private UUID trackingId;
    private String missionNumber;

    // Commercial Ship info
    private UUID commercialShipTrackingId;
    private String commercialShipName;
    private String commercialShipImoNumber;

    // Security Agency info
    private UUID securityAgencyTrackingId;
    private String securityAgencyName;
    private String securityAgencyNumber;

    // Naval Vessel info
    private UUID navalVesselTrackingId;
    private String navalVesselName;
    private String navalVesselNumber;

    // Commander info
    private UUID commanderTrackingId;
    private String commanderName;
    private String commanderRank;

    // Secondary Vessel info (optional)
    private UUID secondaryVesselTrackingId;
    private String secondaryVesselName;

    private String vedettes;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long durationInHours;
    private Integer durationInDays;
    private EscortType escortType;
    private String departurePoint;
    private String arrivalPoint;
    private Double distance;
    private String escortZone;
    private MissionStatus status;
    private String incidents;
    private String observations;
    private LocalDateTime createDate;
}
