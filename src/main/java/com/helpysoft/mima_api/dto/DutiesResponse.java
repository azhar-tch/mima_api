package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.DutyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutiesResponse {
    private UUID trackingId;
    private String position;
    private DutyType dutyType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DutyStatus status;
    private UUID agentTrackingId;
    private UUID agentId;
    private String agentName;
    private UUID unitTrackingId;
    private UUID unitId;
    private String unitName;
    private LocalDateTime createDate;
}
