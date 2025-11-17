package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AgentOtherPositionHistoryResponse {
    private UUID trackingId;
    private UUID agentTrackingId;
    private String agentName;
    private String agentMatricule;
    private UUID otherPositionTrackingId;
    private String positionName;
    private String positionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String remarks;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
