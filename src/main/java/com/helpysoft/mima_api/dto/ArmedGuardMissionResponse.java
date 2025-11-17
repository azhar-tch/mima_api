package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArmedGuardMissionResponse {
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

    private LocalDateTime embarkationDate;
    private LocalDateTime disembarkationDate;
    private String embarkationPort;
    private String disembarkationPort;
    private Integer daysCount;
    private Integer personnelCount;
    private Long durationInHours;
    private String patrolZone;
    private MissionStatus status;
    private String incidents;
    private String observations;
    private LocalDateTime createDate;
}
