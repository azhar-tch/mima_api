package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentTrainingHistoryRequest {
    private UUID agentTrackingId;
    private UUID trainingTrackingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String diploma;
    private String remarks;
}
