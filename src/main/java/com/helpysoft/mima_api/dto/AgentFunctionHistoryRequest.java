package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentFunctionHistoryRequest {
    private UUID agentTrackingId;
    private UUID functionTrackingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String decisionReference;
    private String remarks;
}
