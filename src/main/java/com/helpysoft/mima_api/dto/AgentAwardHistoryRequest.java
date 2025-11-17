package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentAwardHistoryRequest {
    private UUID agentTrackingId;
    private UUID awardTrackingId;
    private LocalDate awardDate;
    private String decisionReference;
    private String motive;
    private String remarks;
}
