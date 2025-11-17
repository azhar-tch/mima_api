package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentOtherPositionHistoryRequest {
    private UUID agentTrackingId;
    private UUID otherPositionTrackingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String remarks;
}
