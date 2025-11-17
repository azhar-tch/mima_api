package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.DutyType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DutiesRequest {
    private String position;
    private DutyType dutyType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DutyStatus status;
    private UUID agentTrackingId;
    private UUID unitTrackingId;
}
