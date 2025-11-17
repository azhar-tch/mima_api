package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AgentTrainingHistoryResponse {
    private UUID trackingId;
    private UUID agentTrackingId;
    private String agentName;
    private String agentMatricule;
    private UUID trainingTrackingId;
    private String trainingName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String diploma;
    private String remarks;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
