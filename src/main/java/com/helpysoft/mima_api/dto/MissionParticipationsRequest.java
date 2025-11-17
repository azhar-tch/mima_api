package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MissionParticipationsRequest {
    private UUID missionTrackingId;
    private UUID agentTrackingId;
    private String missionRule;
    private Double hoursCompleted;
}
