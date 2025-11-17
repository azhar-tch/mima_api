package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentGradeHistoryRequest {
    private UUID agentTrackingId;
    private UUID gradeTrackingId;
    private LocalDate promotionDate;
    private String decisionReference;
    private String remarks;
}
